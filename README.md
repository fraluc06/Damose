# 🚌 Damose

**Damose** è una **desktop app** sviluppata in **Java Swing** per monitorare in tempo reale gli autobus di Roma.

## 🚀 Funzionalità

- Tracciamento in tempo reale dei mezzi pubblici ATAC
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

## ▶️ Esecuzione

Compila ed esegui l'app con un solo comando:

```bash
javac -d out src/com/damose/*.java && java -cp out com.damose.Main
```

Oppure, se utilizzi un IDE (come IntelliJ IDEA o Eclipse), importa il progetto come applicazione Java e avvia il file `Main.java`.

## 📁 Struttura del progetto

```
damose/
├── src/
│   └── com/damose/
│       ├── Main.java
│       ├── TrackerUI.java
│       └── BusService.java
├── README.md
└── ...
```

## 📃 Licenza

Rilasciato sotto licenza **MIT** – vedi il file [LICENSE](LICENSE) per i dettagli.

---

> Made with ❤️ in Rome
