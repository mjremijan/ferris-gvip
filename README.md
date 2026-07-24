# GVIP (GumdoJourney Video Publisher)

A Java application to publish videos to my GumdoJourney YouTube channel.

Quickstart

1. Edit `config/gvip.properties` to set `video.rootDirectory` and `youtube.credentialsFile`.
2. Build with Maven:

```bash
mvn package
```

3. Run the jar:

```bash
java -jar target/gvip-0.1.0-SNAPSHOT.jar --dir "D:\\GumdoJourney\\Videos"
```

This scaffold includes parser stubs for the six filename formats described in the architecture document. Next steps: implement the YouTube integration, `.lnk` resolution, and full upload flow.

Google YouTube integration setup

1. Create OAuth 2.0 client credentials in Google Cloud Console (type: Desktop / Installed application) and download `client_secrets.json`.
2. Place the file at `config/client_secrets.json` or update `config/gvip.properties` `youtube.credentialsFile` to point to it.
3. On first run, the app will open a browser to authorize the account and store tokens in `config/tokens`.

Note: The current implementation uploads videos as `private` by default. Update `YouTubeClientImpl` if you want a different default privacy.
