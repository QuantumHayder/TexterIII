package cmps211.example.texteditor.models;

public class CursorModel {
    /*Approach:

Maintain a list or structure in the JavaFX client that stores each visible character along with its associated UID.

On key typed:

Get the position before the cursor.

Retrieve the UID of the character at that position.

Send that UID as the parentId when broadcasting to /app/type. */
}
