package fr.yxoo;

import fr.yxoo.listeners.Configs;
import me.clip.placeholderapi.libs.kyori.adventure.text.Component;
import me.clip.placeholderapi.libs.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KetherToolsCommand implements CommandExecutor {
    private final KetheriumTools plugin;
    private final Configs config;

    public KetherToolsCommand(KetheriumTools plugin, Configs config) {
        this.plugin = plugin;
        this.config = config;
    }

    private static final String[] [] placeholders =
            {
                    {"jexp_(job)%", "Affichage simple de l'exp actuel dans le job (sans virgules)."},
                    {"jfexp_(job)%", "Affichage simple de l'exp actuel dans le job (formatté)."},
                    {"jlevel_max%", "Affichage simple du niveau le plus haut atteint dans les jobs."},
                    {"jfmaxexp_(job)%", "Affichage simple de l'exp requis dans le job (formatté)."},
                    {"progress_(job)%", "Affichage en pourcentage de la progression actuel dans le job."},
                    {"progressbar_(job)%", "Affichage en barre de progression l'exp du job."},
                    {"jtop_(job)_(number[1-15])%", "Renvois l'UUID du joueur"},
                    {"baltop_uuid_(number)%", "Renvois l'UUID du joueur a cette place dans le baltop."},
                    {"baltop_displayname_(number)%", "Renvois le displayname du joueur a cette place dans le baltop."},
                    {"dimensiontime_{world}_{target_name(placeholder)}%", "Temps restant dans le world."}

            };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kethertools.admin")) {
            sender.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cInvalid argument.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "placeholders":
                handelPlaceholders(sender);
                break;
            default:
                sender.sendMessage("§cCommande inconnue. Utilisation: /kethertools <reload|update>");
                break;
        }

        return true;
    }

    private void handleReload(CommandSender sender) {
        try {
            plugin.onDisable();
            plugin.reloadConfig();
            plugin.reloadConfig();
            plugin.onEnable();
            if (plugin.getConfig().getString(config.reloadMessagePath).length() > 0)
                sender.sendMessage(plugin.getConfig().getString(config.reloadMessagePath).replace("&", "§"));
        } catch (Exception e) {
            sender.sendMessage("§cUne erreur est survenue lors du rechargement du plugin.");
            e.printStackTrace();
        }
    }

    private void handelPlaceholders(CommandSender sender) {
        String key = "%kether_";

        ComponentBuilder message = new ComponentBuilder("§a=== KetherTools Placeholders ===\n");

        for (int i = 0; i < placeholders.length; i++) {
            message.append(key + placeholders[i][0])
                    .color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, key + placeholders[i][0]))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Cliquez pour copier !").color(ChatColor.GRAY).create()))
                    .append(" ➜ ").color(ChatColor.GOLD)
                    .append(placeholders[i][1]).color(ChatColor.WHITE)
                    .append("\n").reset();
        }

        message.append("§a===============================");

        sender.spigot().sendMessage(message.create());
    }

}