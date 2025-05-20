package it.uniroma.di.mdp.francesco;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.security.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StaticGTFSDownloader {

    private final String fileURL;
    private final String md5URL;
    private final Path folderPath;
    private final Path localFile;
    private final HttpClient httpClient;

    public StaticGTFSDownloader(String fileURL, String md5URL, Path folderPath) {
        this.fileURL = fileURL;
        this.md5URL = md5URL;
        this.folderPath = folderPath;
        this.localFile = folderPath.resolve(getFileNameFromURL(fileURL));
        this.httpClient = HttpClient.newHttpClient();
    }

    // Verifica se il file locale è aggiornato
    public boolean isUpToDate() throws IOException, InterruptedException, NoSuchAlgorithmException {
        if (!Files.exists(localFile)) return false;

        String remoteMd5 = downloadAsString(md5URL).split("\\s+")[0];
        String localMd5 = calculateMD5(localFile);
//        System.out.println("MD5 remoto: " + remoteMd5);
//        System.out.println("MD5 locale: " + localMd5);
        return localMd5.equalsIgnoreCase(remoteMd5);
    }

    // Scarica il file ZIP se non aggiornato e lo estrae
    public boolean downloadAndUnzipIfNeeded() throws IOException, InterruptedException, NoSuchAlgorithmException {
        Files.createDirectories(folderPath);

        if (!isUpToDate()) {
            // Scarica il file ZIP usando HttpClient e lo carica in un file temporaneo
            Path tempFile = localFile.resolveSibling(localFile.getFileName() + ".tmp");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileURL))
                    .build();

            HttpResponse<Path> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofFile(tempFile));

            if (response.statusCode() == 200) {
                // Sovrascrive il file solo se il download è andato a buon fine
                Files.move(tempFile, localFile, StandardCopyOption.REPLACE_EXISTING);
                unzipFile(localFile, folderPath);
                return true;
            } else {
                Files.deleteIfExists(tempFile);
                throw new IOException("Download failed, status code: " + response.statusCode());
            }
        }

        return false; // già aggiornato, nessun download effettuato
    }


    // Metodo per leggere il contenuto testuale da un URL (il file .md5 remoto)
    private String downloadAsString(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("Failed to download MD5, status code: " + response.statusCode());
        }
    }

    // Calcola MD5 di un file
    private String calculateMD5(Path filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(filePath);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[4096];
            while (dis.read(buffer) != -1) { }
        }
        byte[] digestBytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digestBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // Estrae tutti i file da uno ZIP, sovrascrivendo quelli esistenti
    public void unzipFile(Path zipFilePath, Path targetDir) throws IOException {
        Files.createDirectories(targetDir);

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path outPath = targetDir.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(outPath);
                } else {
                    Files.createDirectories(outPath.getParent());
                    try (OutputStream out = Files.newOutputStream(outPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    // Utility per estrarre il nome del file dall'URL
    private String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
