# PrisonEscape Test Server

Welcome to the PrisonEscape Test Server repository! This repository contains all the necessary files to set up a Minecraft server for testing the PrisonEscape plugin. Please note that only files with relevant changes are included here. Additional setup and configuration may be required.

## Prerequisites

To set up the Minecraft server, make sure you have the following:

- Java installed on your system
- Minecraft

## Setup Instructions

### 1. Download the Minecraft Server JAR

1. Download the Paper server JAR [PaperMC Downloads version 1.20.4-462](https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/462/downloads/paper-1.20.4-462.jar).
2. Place the downloaded JAR file in this directory.
3. Run it.

### 2. Initial Set-Up

1. **Edit `eula.txt`**:
   - Open the `eula.txt` file.
   - Change `eula=false` to `eula=true`.
   - Save the file.
2. **Run `run.bat` to Start the Server**.
3. **Stop the Server** after it starts successfully by typing `stop` in the console and pressing Enter.
4. **Add the WorldEdit Plugin**:
   - Download the latest WorldEdit JAR from [CurseForge](https://dev.bukkit.org/projects/worldedit).
   - Place the downloaded JAR file in the `plugins` folder.
5. **Install a World Management Plugin like Multiverse-Core**:
   - Download the Multiverse-Core JAR.
   - Place the downloaded JAR file in the `plugins` folder.

### 3. Enter Local Server

1. **Compile the PrisonEscape Plugin using Maven**:
    ```s
    mvn install
    ```
2. **Copy the Generated JAR File** from the `target` directory to the `TestServer/plugins` folder.
3. **Run `run.bat` to Start the Server**.
4. **Open Minecraft**, and add the PrisonEscape server to multiplayer using "localhost".
5. **Enter the Game**. In the server console (run.bat), type the following command to give yourself admin permissions:
    ```s
    op YourMinecraftUsername
    ```
   Replace `YourMinecraftUsername` with your actual Minecraft username.
6. **In the Minecraft Console**, run the command to import the world:
    ```s
    /mv import PrisonEscapeWorld normal
    ```
7. **Start the Game** with the following command:
    ```s
    /pe start
    ```
8. **Compiling Changes**: Every time you make a change to the code, you need to compile it again and place the updated JAR file in the `plugins` folder.

## Troubleshooting

### Common Issues

- **Server Fails to Start**:
  - Ensure correct Java version.
  - Verify `paper.jar` file.
  - Check `eula.txt` for `eula=true`.
- **Plugin Not Working**:
  - Ensure plugins are in the `plugins` folder.
  - Check server console for errors.
  - Verify plugin compatibility with PaperMC version.
- **Map is Null Error**:
  - Ensure Multiverse-Core is installed correctly.
  - Import the map using `/mv import PrisonEscapeWorld normal`.
  - Verify `config.yml` settings. 
  - Ensure compatibility with the PrisonEscape plugin's `config.yml`.
- **MessageLanguageManager Tries to Get Null Value**:
  - Check TestServer's `english.yml` file for updates.
  - Ensure compatibility with the PrisonEscape plugin's `english.yml`.
  - Do the same for other language files.

### Getting Help

For further assistance:
- Review server logs for specific error messages.
- Consult [PaperMC Documentation](https://papermc.io/documentation) for server-related issues.
- Refer to [Multiverse-Core Wiki](https://github.com/Multiverse/Multiverse-Core/wiki) for help with world management    .
- Feel free to mention us in a discussion board for plugin-related issues.

## Contributing

If you make changes, please push only relevant files to the repository. We're excited to see your contributions!

## License

This project is licensed under the GNU General Public License (GPL). See the [LICENSE](../LICENSE) file for details.