# 🚌 Damose

**Damose** è una **desktop app** sviluppata in **Java Swing** per monitorare il trasporto pubblico di Roma.

## 🚀 Funzionalità

- Tracciamento statico o in tempo reale dei mezzi pubblici ATAC
- Interfaccia desktop leggera e intuitiva
- Ricerca per linea o fermata
- Visualizzazione delle stime di arrivo

## 🛠️ Tecnologie utilizzate

- **Java 21 LTS**
- **Swing** per l'interfaccia grafica
- Dati GTFS statici e RT di ATAC

## 📦 Requisiti

- JDK 17 o superiore
- Connessione a Internet attiva (per i dati in tempo reale)

## 📁 Struttura del progetto

```
Damose/
...
├── src/
│   └── it/uniroma/di/mdp/francesco/damose/
│       ├── BusWaypoint.java
│       ├── GlobalParameters.java
        ├── Favouritess.java
│       ├── GTFSFetcher.java
│       ├── Main.java
│       ├── OnlineStatusChecker.java
│       ├── Route.java
│       ├── Routes.java
│       ├── StaticGTFSDownloader.java
│       ├── Stop.java
│       ├── Stops.java
│       ├── StopTime.java
│       ├── StopTimes.java
│       ├── Trip.java
│       ├── Trips.java
├── LICENSE.txt
├── README.md
└── ...
```

## 📃 Licenza

Rilasciato sotto licenza **GPLv3** – vedi il file [LICENSE](LICENSE.txt) per i dettagli.

---

> Made with ❤️ in Rome by Francesco Lucarelli ([**_fraluc06_**](https://github.com/fraluc06))