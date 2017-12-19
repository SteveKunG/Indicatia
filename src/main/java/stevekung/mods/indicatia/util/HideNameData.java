package stevekung.mods.indicatia.util;

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
                list.appendTag(tag);
            }
        }
        return list;
    }

    public static void load(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            String name = tag.getString(String.valueOf(i));
            HideNameData.stringList.add(i, name);
        }
    }

    public static List<String> getHideNameList()
    {
        return HideNameData.stringList;
    }
}