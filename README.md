# 🚌 Damose

**Damose** è una **desktop app** sviluppata in **Java Swing** per monitorare il trasporto pubblico di Roma.

## 🚀 Funzionalità

- Tracciamento statico o in tempo reale dei mezzi pubblici ATAC
- Interfaccia desktop leggera e intuitiva
- Ricerca per linea o fermata
- Visualizzazione delle stime di arrivo

## 🛠️ Tecnologie utilizzate

- **Java 17+**
- **Swing** per l'interfaccia grafica
- API ATAC (o altro servizio di trasporto pubblico)

## 📦 Requisiti

- JDK 17 o superiore
- Connessione a Internet attiva

## 📁 Struttura del progetto

```
Damose/
...
├── src/
│   └── it/uniroma/di/mdp/francesco/damose/
│       ├── BusWaypoint.java
│       ├── GlobalParameters.java
│       ├── GTFSFetcher.java
│       ├── MainPage.java
│       ├── Route.java
│       ├── Routes.java
│        ├── StaticGTFSDownloader.java
│       ├── Stop.java
│       ├── Stops.java
│       ├── StopTime.java
│       ├── StopTimes.java
│       ├── Trip.java
│       ├── Trips.java
│       └── BusService.java
├── LICENSE.txt
├── README.md
└── ...
```

## 📃 Licenza

Rilasciato sotto licenza **GPLv3** – vedi il file [LICENSE](LICENSE.txt) per i dettagli.

---

> Made with ❤️ in Rome by Francesco Lucarelli ([**_fraluc06_**](https://github.com/fraluc06))