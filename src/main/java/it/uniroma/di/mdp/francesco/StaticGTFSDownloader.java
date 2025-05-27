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

        /**
         * Classe che si occupa di scaricare, verificare e decomprimere i dati GTFS statici.
         * Gestisce il download del file ZIP, il controllo dell'MD5 e l'estrazione dei file.
         */
        public class StaticGTFSDownloader {

            /** URL del file GTFS statico. */
            private final String fileURL;
            /** URL del file MD5 associato al GTFS. */
            private final String md5URL;
            /** Percorso della cartella di destinazione dei dati estratti. */
            private final Path folderPath;
            /** Percorso locale del file ZIP scaricato. */
            private final Path localFile;
            /** HttpClient utilizzato per i download. */
            private final HttpClient httpClient;

            /**
             * Costruttore della classe StaticGTFSDownloader.
             * @param fileURL URL del file GTFS statico
             * @param md5URL URL del file MD5
             * @param folderPath percorso della cartella di destinazione
             */
            public StaticGTFSDownloader(String fileURL, String md5URL, Path folderPath) {
                this.fileURL = fileURL;
                this.md5URL = md5URL;
                this.folderPath = folderPath;
                this.localFile = folderPath.resolve(getFileNameFromURL(fileURL));
                this.httpClient = HttpClient.newHttpClient();
            }

            /**
             * Verifica se il file locale è aggiornato confrontando l'MD5 remoto e locale.
             * @return true se il file è aggiornato, false altrimenti
             * @throws IOException in caso di errore di I/O
             * @throws InterruptedException in caso di interruzione del thread
             * @throws NoSuchAlgorithmException se l'algoritmo MD5 non è disponibile
             */
            public boolean isUpToDate() throws IOException, InterruptedException, NoSuchAlgorithmException {
                if (!Files.exists(localFile)) return false;

                String remoteMd5 = downloadAsString(md5URL).split("\\s+")[0];
                String localMd5 = calculateMD5(localFile);
                return localMd5.equalsIgnoreCase(remoteMd5);
            }

            /**
             * Scarica il file ZIP se non aggiornato e lo estrae nella cartella di destinazione.
             * @return true se è stato effettuato il download, false se già aggiornato
             * @throws IOException in caso di errore di I/O
             * @throws InterruptedException in caso di interruzione del thread
             * @throws NoSuchAlgorithmException se l'algoritmo MD5 non è disponibile
             */
            public boolean downloadAndUnzipIfNeeded() throws IOException, InterruptedException, NoSuchAlgorithmException {
                Files.createDirectories(folderPath);

                if (!isUpToDate()) {
                    Path tempFile = localFile.resolveSibling(localFile.getFileName() + ".tmp");
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(fileURL))
                            .build();

                    HttpResponse<Path> response = httpClient.send(request,
                            HttpResponse.BodyHandlers.ofFile(tempFile));

                    if (response.statusCode() == 200) {
                        Files.move(tempFile, localFile, StandardCopyOption.REPLACE_EXISTING);
                        unzipFile(localFile, folderPath);
                        return true;
                    } else {
                        Files.deleteIfExists(tempFile);
                        throw new IOException("Download failed, status code: " + response.statusCode());
                    }
                }

                return false;
            }

            /**
             * Estrae tutti i file da uno ZIP, sovrascrivendo quelli esistenti.
             * @param zipFilePath percorso del file ZIP
             * @param targetDir cartella di destinazione
             * @throws IOException in caso di errore di I/O
             */
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

            // Metodo privato per leggere il contenuto testuale da un URL (il file .md5 remoto)
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

            // Metodo privato per calcolare l'MD5 di un file
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

            // Utility per estrarre il nome del file dall'URL
            private String getFileNameFromURL(String url) {
                return url.substring(url.lastIndexOf('/') + 1);
            }
        }