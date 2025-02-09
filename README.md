# Prison Escape Minigame Plugin

This Minecraft plugin replicates "Prison Life" game on Steam. Players join a server with 3-11 other players, each receiving a random role. The objective is simple: prisoners must work together to escape the prison within 4 days, while guards must prevent them from doing so.

## Contributing

Contributions to the development of this plugin are welcome! If you encounter any bugs or have suggestions for new features, please open an issue or submit a pull request on GitHub.

Please note that if you want to work on an issue, leave a comment, and we will assign it to you.

### Building

To build PrisonEscape, you need JDK 17 or higher installed on your system.

Clone this repository, then run the following command:

```s
mvn install
```

You can then find the .jar file of PrisonEscape in the `target/` directory.

### Formatting

This project uses [Spotless](https://github.com/diffplug/spotless) to ensure code formatting rules
are followed.

It is possible to check if the code adheres to the formatting rules by running:

```s
mvn spotless:check
```

Or, alternatively, format the code:

```s
mvn spotless:apply
```

To ensure consistency and maintainability, please make sure that your code is properly formatted and compiles successfully before submitting a pull request.

### Running a test server

For a more in detail explanation on how to run your local copy please go to the [TestServer folder](/TestServer/).

**Thank you for your contributions!** 😊

## License

This project is licensed under the GNU General Public License (GPL). See the [LICENSE](LICENSE) file for details.