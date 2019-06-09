package stevekung.mods.indicatia.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class HideNameData
{
    private static final List<String> stringList = new ArrayList<>();

    public static ListNBT save()
    {
        ListNBT list = new ListNBT();

        for (int i = 0; i < HideNameData.stringList.size(); i++)
        {
            String name = HideNameData.stringList.get(i);

            if (name != null)
            {
                CompoundNBT tag = new CompoundNBT();
                tag.putString(String.valueOf(i), name);
                list.add(tag);
            }
        }
        return list;
    }

    public static void load(ListNBT list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            CompoundNBT tag = list.getCompound(i);
            String name = tag.getString(String.valueOf(i));
            HideNameData.stringList.add(i, name);
        }
    }

    public static List<String> getHideNameList()
    {
        return HideNameData.stringList;
    }
}