package top.ithilelda.sustenance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();

		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("sustenance.yaml");
		try {
			if (Files.exists(configPath)) {
				Config = mapper.readValue(Files.readString(configPath), Configuration.class);
			}
			else {
				Files.createFile(configPath);
				Files.writeString(configPath, mapper.writeValueAsString(Config));
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
