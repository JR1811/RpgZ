package net.rpgz;

import net.fabricmc.api.ModInitializer;
import net.rpgz.init.ConfigInit;
import net.rpgz.init.SoundInit;
import net.rpgz.init.TagInit;
import net.shirojr.nemuelch.NeMuelch;

public class RpgzMain implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigInit.init();
        SoundInit.init();
        TagInit.init();

        NeMuelch.LOGGER.info("A modified version of RpgZ has been installed. For more information, see https://github.com/JR1811/RpgZ");
    }
}