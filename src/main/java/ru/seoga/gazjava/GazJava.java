package ru.seoga.gazjava;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GazJava implements ModInitializer {
    public static final String MOD_ID = "gazjava";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Item NEFOR = register("gazjava_nefora", 60);
    public static final Item OFNIK = register("gazjava_ofnika", 300);
    public static final Item MOTOBRAT = register("gazjava_motobrata", 1200);

    private static Item register(String name, int seconds) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), new GazJavaDrinkItem(new Item.Settings().maxCount(16), seconds));
    }

    @Override public void onInitialize() {
        LOGGER.info("GazJava: три напитка готовы к употреблению!");
    }
}
