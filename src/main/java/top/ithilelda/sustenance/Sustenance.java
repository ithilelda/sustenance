package top.ithilelda.sustenance;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Sustenance implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("sustenance");
	public static Configuration Config = new Configuration();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Sustenance initialized.");
		Yaml yaml = new Yaml();

		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("sustenance.yaml");
		try {
			if (Files.exists(configPath)) {
				Config = yaml.loadAs(Files.readString(configPath), Configuration.class);
				Files.writeString(configPath, yaml.dumpAs(Config, Tag.MAP, DumperOptions.FlowStyle.BLOCK)); // dump again for updated configs.
			}
			else {
				Files.createFile(configPath);
				Files.writeString(configPath, yaml.dumpAs(Config, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
			Config = new Configuration(); // generate a new default just to be sure.
            try {
                Files.writeString(configPath, yaml.dumpAs(Config, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
            } catch (IOException ex) {
				LOGGER.error(ex.getMessage());
            }
        }
	}
}
