package com.xiii.libertycity.roleplay.items.wallet.data;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DataManager {

    public String encodeBase64(final Inventory inventory) {

        try {

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            outputStream.flush();
            outputStream.close();
            dataOutput.flush();
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }


    public Inventory decodeBase64(final String data) {

        try {

            final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            final Inventory inventory = Bukkit.createInventory(null, dataInput.readInt(), "§8Porte feuille");

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            inputStream.close();

            return inventory;
        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();
        }

        return null;
    }
}
