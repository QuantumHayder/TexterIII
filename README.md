# 📖 Texter: Collaborative Text Editor  

Welcome to **Texter**, a collaborative text editor built in Java using the Spring Boot framework! This application allows multiple users to edit documents in real-time, making collaboration seamless and efficient.   

## 🚀 Project Overview  

**Texter** leverages a 🌳 tree-based Conflict-free Replicated Data Type (CRDT) for resolving merge conflicts and utilizes WebSockets for real-time editing. An H2 database is employed for storing documents.   

### 📝 Project Specifications

- 📂 **Import/Export Files**: Users can import/export `.txt` files while preserving line breaks.  
- 🔗 **Sharing**: Users can request sharable codes from the application server. Each file has two codes: one for editors and another for read-only collaborators. Collaboration sessions can be started by sharing these codes through WhatsApp, email, etc.  
- ✍️ **Character-Based Editing**: Support character-by-character insertion and deletion, including pasting text as multiple insertions.  
- 🔄 **Concurrent Edits**: Use a tree-based CRDT algorithm to manage concurrency and conflicts due to simultaneous edits by different users.  
- ⚡ **Real-time Updates**: Send and receive edits in real-time, relayed through a central server.  
- 👥 **Cursor Tracking & User Presence**: Update cursor positions of other users and display a list of active users in the collaboration session.  
- ↩️ **Undo/Redo Changes**: Implement an undo/redo functionality that allows users to revert their last three actions (but not those of other users).  
- 📝 **Text Area**: A display area for the document with editing capabilities.  
- 📤 **Sharing Options**: Show the shareable codes to the user.  
- 🤝 **Collaboration Join**: Enable users to join a collaboration session.  
- 🔒 **Permission Handling**: Ensure that viewers cannot edit the text area or view sharable codes.  
- 🖱️ **Cursor Display & User Presence**: Visually represent other users' cursors with distinct colors and show a list of active users. Assume a maximum of four concurrent editors per document.  
- 💬 **User Comments**: Editors can add comments to specific text sections that are removed when the text is deleted.  
- 🔄 **Reconnection Support**: Allow users a 5-minute window to reconnect after a network drop, with missed edits sent upon reconnection.  

---  

Thank you for checking out **Texter**! We’re excited to see how you collaborate and create amazing documents together! 🎉  
