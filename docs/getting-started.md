# Getting started

1. Edit `config/gvip.properties`, setting values for all properties
   
2. Google YouTube integration setup
   1. Create OAuth 2.0 client credentials in Google Cloud Console (type: Desktop / Installed application) and download `client_secrets.json`.
   2. Place the file where specified in `config/gvip.properties` `youtube.credentialsFile`.
   3. On first run, the app will open a browser to authorize the account and store tokens where specified with `config/gvip.properties` `oauth.tokensDir`.
   
3. Build with Maven:
   
```bash
mvn package
```
4. Run the jar:

```bash
java -jar target/gvip-0.1.0-SNAPSHOT.jar
```


