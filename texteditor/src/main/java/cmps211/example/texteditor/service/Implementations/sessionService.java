package cmps211.example.texteditor.service.Implementations;

public class sessionService {
    /*✅ 2. Can You Access the Client ID from the JavaFX Side?
Yes — but only if you give it to the client when it connects/logs in.

➤ JavaFX by default knows nothing about the backend's ClientModel.
So you must:

When the JavaFX app connects or logs in:

Either send username & mode → get back a ClientModel (with UID),

Or let the server create a new ClientModel, and return the UID.

Save that UID on the JavaFX side (in a session variable or memory).

When sending a character via WebSocket: */

//String[] payload = {String.valueOf(clientId), docId, String.valueOf(typedChar), parentUID};

}
