package com.github.ipecter.rtustudio.varmor.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.ipecter.rtustudio.varmor.VanishArmor;
import com.github.ipecter.rtustudio.varmor.config.VanishConfig;
import com.github.ipecter.rtustudio.varmor.manager.ToggleManager;
import kr.rtuserver.framework.bukkit.api.dependencies.RSPacketListener;
import com.github.ipecter.rtustudio.varmor.protocol.wrapper.WrapperPlayServerEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayServerEntityEquipmentListener extends RSPacketListener<VanishArmor> {

    private final VanishConfig config;
    private final ToggleManager manager;

    public PlayServerEntityEquipmentListener(VanishArmor plugin) {
        super(plugin, new AdapterParameteters()
                .listenerPriority(ListenerPriority.HIGHEST)
                .types(PacketType.Play.Server.ENTITY_EQUIPMENT)
                .optionAsync());
        this.config = plugin.getVanishConfig();
        this.manager = plugin.getToggleManager();
    }

    private final ItemStack empty = new ItemStack(Material.AIR);

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperPlayServerEntityEquipment p = new WrapperPlayServerEntityEquipment(packet);
        if (config.isHideOther()) {
            if (check(p.getEntityID())) {
                Player player = event.getPlayer();
                if (!manager.getMap().getOrDefault(player.getUniqueId(), false)) return;
                if (!player.hasPermission(getPlugin().getName() + ".vanish")) return;
                p.setSlotStackPair(EnumWrappers.ItemSlot.HEAD, empty);
                p.setSlotStackPair(EnumWrappers.ItemSlot.CHEST, empty);
                p.setSlotStackPair(EnumWrappers.ItemSlot.LEGS, empty);
                p.setSlotStackPair(EnumWrappers.ItemSlot.FEET, empty);
            }
        }
        if (config.isHideFromOther()) {
            if (check(p.getEntityID())) {
                Player player = (Player) p.getEntity(event);
                if (!manager.getMap().getOrDefault(player.getUniqueId(), false)) return;
                if (!player.hasPermission(getPlugin().getName() + ".vanish")) return;
                p.setSlotStackPair(EnumWrappers.ItemSlot.HEAD, empty);
                p.setSlotStackPair(EnumWrappers.ItemSlot.CHEST, empty);
                p.setSlotStackPair(EnumWrappers.ItemSlot.LEGS, empty);
                p.setSlotStackPair(EnumWrappers.ItemSlot.FEET, empty);
            }
        }
    }

    private boolean check(int id) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (id == player.getEntityId()) return true;
        }
        return false;
    }
}
