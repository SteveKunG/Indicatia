package stevekung.mods.stevekunglib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class BlockItemRemapper
{
    public static void remapBlock(RegistryEvent.MissingMappings<Block> event, String modid, String oldName, Block block)
    {
        for (RegistryEvent.MissingMappings.Mapping<Block> mappings : event.getMappings())
        {
            if (mappings.key.getResourceDomain().equals(modid) && mappings.key.getResourcePath().equals(oldName))
            {
                mappings.remap(block);
                ModLogger.info("Remapping Block Complete (From {} to {})", mappings.key, block.getRegistryName());
            }
        }
    }

    public static void remapItem(RegistryEvent.MissingMappings<Item> event, String modid, String oldName, Block block)
    {
        for (RegistryEvent.MissingMappings.Mapping<Item> mappings : event.getMappings())
        {
            if (mappings.key.getResourceDomain().equals(modid) && mappings.key.getResourcePath().equals(oldName))
            {
                mappings.remap(Item.getItemFromBlock(block));
                ModLogger.info("Remapping Block Complete (From {} to {})", mappings.key, block.getRegistryName());
            }
        }
    }

    public static void remapItem(RegistryEvent.MissingMappings<Item> event, String modid, String oldName, Item item)
    {
        for (RegistryEvent.MissingMappings.Mapping<Item> mappings : event.getMappings())
        {
            if (mappings.key.getResourceDomain().equals(modid) && mappings.key.getResourcePath().equals(oldName))
            {
                mappings.remap(item);
                ModLogger.info("Remapping Block Complete (From {} to {})", mappings.key, item.getRegistryName());
            }
        }
    }
}