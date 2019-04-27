package stevekung.mods.indicatia.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class HideNameData
{
    private static final List<String> stringList = new ArrayList<>();

    public static ListTag save()
    {
        ListTag list = new ListTag();

        for (int i = 0; i < HideNameData.stringList.size(); i++)
        {
            String name = HideNameData.stringList.get(i);

            if (name != null)
            {
                CompoundTag tag = new CompoundTag();
                tag.putString(String.valueOf(i), name);
                list.add(tag);
            }
        }
        return list;
    }

    public static void load(ListTag list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            CompoundTag tag = list.getCompoundTag(i);
            String name = tag.getString(String.valueOf(i));
            HideNameData.stringList.add(i, name);
        }
    }

    public static List<String> getHideNameList()
    {
        return HideNameData.stringList;
    }
}