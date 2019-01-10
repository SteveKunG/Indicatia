package stevekung.mods.indicatia.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class HideNameData
{
    private static final List<String> stringList = new ArrayList<>();

    public static NBTTagList save()
    {
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < HideNameData.stringList.size(); i++)
        {
            String name = HideNameData.stringList.get(i);

            if (name != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString(String.valueOf(i), name);
                list.add(tag);
            }
        }
        return list;
    }

    public static void load(NBTTagList list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            NBTTagCompound tag = list.getCompound(i);
            String name = tag.getString(String.valueOf(i));
            HideNameData.stringList.add(i, name);
        }
    }

    public static List<String> getHideNameList()
    {
        return HideNameData.stringList;
    }
}