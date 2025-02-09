# PrisonEscape Test Server

Welcome to the PrisonEscape Test Server repository! This repository contains all the necessary files to set up a Minecraft server for testing the PrisonEscape plugin. Please note that only files with relevant changes are included here. Additional setup and configuration may be required.

## Prerequisites

To set up the Minecraft server, make sure you have the following:

- Java installed on your system
- Minecraft

## Setup Instructions

### 1. Download the Minecraft Server JAR

1. Download the most recent Paper server JAR [PaperMC Downloads](https://papermc.io/downloads/paper).
2. Place the downloaded JAR file in this directory.
3. Create a run.bat file and add this content inside:
    ```s
    @ECHO OFF
    java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar <your_paper_file_name>.jar nogui
    PAUSE
    ```
4. Execute run.bat file.

### 2. Initial Set-Up

1. Edit `eula.txt`:
   - Open the `eula.txt` file.
   - Change `eula=false` to `eula=true`.
   - Save the file.
2. Run `run.bat` to Start the Server.
3. Stop the Server after it starts successfully by typing `stop` in the console and pressing Enter.
4. Add the WorldEdit Plugin:
   - Download the latest WorldEdit JAR from [Bukkit](https://dev.bukkit.org/projects/worldedit).
   - Place the downloaded JAR file in the `plugins` folder.
5. Install a World Management Plugin like Multiverse-Core:
   - Download the Multiverse-Core JAR from [Bukkit](https://dev.bukkit.org/projects/multiverse-core).
   - Place the downloaded JAR file in the `plugins` folder.
6. Install a Void Generator Plugin like VoidWorld:
   - Download the Multiverse-Core JAR from [Bukkit](https://dev.bukkit.org/projects/voidworld).
   - Place the downloaded JAR file in the `plugins` folder.

### 3. Enter Local Server

1. Compile the PrisonEscape Plugin using Maven:
    ```s
    mvn install
    ```
2. Copy the Generated JAR File** from the `target` directory to the `TestServer/plugins` folder.
3. Run `run.bat` to Start the Server.
4. Add and join the PrisonEscape server to multiplayer using "localhost" as the address.
5. In the server console, type the following command to give yourself admin permissions:
    ```s
    op <YourMinecraftUsername>
    ```
6. Run the command to create a void world:
    ```s
    /mv create PrisonEscapeWorld normal -g VoidGenerator
    ```
7. Load maps with the following command:
    ```s
    /pe loadmaps
    ```
8. Start game using the following command:
    ```s
    /pe join
    ```

## Contributing

If you make changes, please push only relevant files to the repository. We're excited to see your contributions!

## License

This project is licensed under the GNU General Public License (GPL). See the [LICENSE](../LICENSE) file for details.