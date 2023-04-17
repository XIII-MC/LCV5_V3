package com.xiii.libertycity.roleplay.events.network;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.libertycity.LibertyCity;
import com.xiii.libertycity.core.enums.MsgType;
import com.xiii.libertycity.core.manager.files.FileManager;
import com.xiii.libertycity.core.manager.profile.Profile;
import com.xiii.libertycity.core.manager.profile.utils.ProfileUtils;
import com.xiii.libertycity.core.processors.network.packet.ClientPlayPacket;
import com.xiii.libertycity.core.processors.network.packet.ServerPlayPacket;
import com.xiii.libertycity.core.utils.ChatUtils;
import com.xiii.libertycity.core.utils.time.TimeFormat;
import com.xiii.libertycity.core.utils.time.TimeUtils;
import com.xiii.libertycity.roleplay.events.Data;
import com.xiii.libertycity.roleplay.items.idcard.IDCardManager;
import com.xiii.libertycity.roleplay.utils.PhoneNumber;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RegisterEvent implements Data {

    private static String temp_rpFirstName, temp_rpLastName, temp_rpAge;
    private static boolean awaitResponse_rpFirstName, awaitResponse_rpLastName, awaitResponse_rpAge;
    //private final static List<String> BANNED_CHARS = Arrays.asList("&", "~", "\"", "#", "'", "{", "(", "[", "-", "|", "`", "_", "\\", "^", "@", "°", ")", "]", "=", "+", "}", "£", "$", "¤", "^", "¨", "*", "µ", "ù", "%", "!", "§", ":", "/", ";", ".", ",", "?", "<", ">", "²", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    public void handle(final ClientPlayPacket packet) {

        final Player player = packet.getPlayer();
        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (packet.getType() == PacketType.Play.Client.CHAT_MESSAGE && !profile.isVerified) {

            packet.getEvent().setCancelled(true);

            final String message = packet.getChatWrapper().getMessage();

            final String playerEcho = "§a§l" + temp_rpFirstName + "§r §2§l" + temp_rpLastName + "§r§7: §f";

            player.sendMessage(playerEcho.replace("null", "§K??????") + message);

            //Rp prénom
            if (temp_rpFirstName == null && profile.rpFirstName == null) {

                if (message.length() >= 3 && message.length() <= 18) {

                    if (letterOnly(message)) {

                        temp_rpFirstName = message;
                        awaitResponse_rpFirstName = true;
                        player.sendMessage(MsgType.STEVE_HIDDEN.getMessage() + "Mmmh... Alors si j'ai bien compris, tu t'appel §a§l" + temp_rpFirstName + "§r, c'est bien ça? §7(Oui/Non)");
                    } else player.sendMessage(MsgType.STEVE_HIDDEN.getMessage() + "§cHop hop hop gamin! J'suis pas une machine moi, depuis quand on met autre chose que des lettres dans un prénom?! Tu tes pris pour Elon Musk ou quoi? Bon alors, tu veut bien me la refaire en français cette fois?");

                } else player.sendMessage(MsgType.STEVE_HIDDEN.getMessage() + "§cOula gamin! Tu nous fait quoi la? J'te demande le prénom le plus long ni le plus court du monde! Tu peut la refaire version plus 'normal' entre 3 et 18 lettres?");
                return;
            }

            //Confirm tempFirstName
            if (temp_rpFirstName != null && awaitResponse_rpFirstName && profile.rpFirstName == null) {

                if (message.contains("oui")) {

                    profile.rpFirstName = temp_rpFirstName;
                    player.sendTitle("", "§7§oContinuez par dire votre nom à §e§oSteve§7.", 320, 900*20, 2*20);
                    player.sendMessage(MsgType.STEVE_FIRST.getMessage() + "Bah bien content de faire ta conaissance mon ptit... §a§l" + temp_rpFirstName + "§r, eh scuse moi j'ai oublié de me présenter! Moi c'est §eSteve§r, tu m'excusera d'ailleurs j'ai un peut la mémoire courte des fois... Ceci dit j'me fait plus si jeune que sa moi! Bon sinon ma poule raconte m'en plus! C'est quoi ton ptit §6nom a toi?");
                } else if (message.contains("non")) {

                    shutdown(player);
                    player.sendMessage(MsgType.STEVE_HIDDEN.getMessage() + "§cAh bah tiens j'suis pas le seul a perdre la tête! Bon aller on va reprendre depuis le tous début pour être sur.");
                    player.sendTitle("", "§7§oCommencez par dire votre prénom au barman.", 3*20, 900*20, 2*20);
                }
                return;
            }

            //Rp nom
            if (temp_rpLastName == null && profile.rpLastName == null) {

                if (message.length() >= 3 && message.length() <= 18) {

                    if (letterOnly(message)) {

                        temp_rpLastName = message;
                        awaitResponse_rpLastName = true;
                        player.sendMessage(MsgType.STEVE_FIRST.getMessage() + "Ah bon? Tu t'appel vraiment §a§l" + profile.rpFirstName + " §2§l" + temp_rpLastName + "§r? Sa me rappel qu'unlqun... §7(Oui/Non)");
                    } else player.sendMessage(MsgType.STEVE_FIRST.getMessage() + "§cNan mais oh, tu sais pas écrire ou quoi?! Un nom c'est des LETTRES, DES LETTRES! On est pas en cours de maths ici hien! J'vais pas te demander de me calculer x ou y! Alors refait la en français sans ton problème machin de maths");
                } else player.sendMessage(MsgType.STEVE_FIRST.getMessage() + "§cMais dis donc mon vieux c'est que tchache bien! Mais la j'aimerais bien juste connaître ton nom, juste ton nom oui pas ton arbre généalogique... Alors si tu voudrais bien la refaire mais entre 3 et 18 caractères, je prend!");
                return;
            }

            //Confirm tempLastName
            if (temp_rpLastName != null && awaitResponse_rpLastName && profile.rpLastName == null) {

                if (message.contains("oui")) {

                    if (!ProfileUtils.isNameAvailable(temp_rpFirstName, temp_rpLastName)) {

                        shutdown(player);
                        player.sendMessage(MsgType.STEVE_FIRST.getMessage() + "§cT'essairais pas de te faire passer pour qunlqu'un d'autre par hasard? Je connais déja qunlqu'un qui s'appel comme sa dans la ville...Bon aller on va reprendre depuis le tous début pour être sur.");
                        player.sendTitle("", "§7§oCommencez par dire votre prénom au barman.", 3 * 20, 900 * 20, 2 * 20);
                        return;
                    }

                    ProfileUtils.getDataBaseRPFirstNames().put(temp_rpFirstName, player.getUniqueId());
                    ProfileUtils.getDataBaseRPLastNames().put(temp_rpLastName, player.getUniqueId());
                    ProfileUtils.updateServerProfile();

                    profile.rpLastName = temp_rpLastName;
                    player.sendTitle("", "§7§oFinissez par dire votre âge à §e§oSteve Rowland§7.", 3 * 20, 900 * 20, 2 * 20);
                    player.sendMessage(MsgType.STEVE_FULL.getMessage() + "Nan c'est pas vrai... §a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + "§r c'est toi?! MICHEL VIENS VOIR! J'en crois pas mes yeux, sa fait un baille que je me demande ou t'était partis! Tu te souviens peut être de moi tiens, c'est moi §eSteve Rowland§r, je fesais partis du groupe de Hackeur ya quelque années de sa. Ta du prendre un ptit coup de vieux depuis, nan? Ta quel §6age§r maintenant dis donc?");
                } else if (message.contains("non")) {

                    shutdown(player);
                    player.sendMessage(MsgType.STEVE_FIRST.getMessage() + "§cAh bah tiens j'suis pas le seul a perdre la tête! Bon aller on va reprendre depuis le tous début pour être sur.");
                    player.sendTitle("", "§7§oCommencez par dire votre prénom au barman.", 3*20, 900*20, 2*20);
                }
                return;
            }

            //Rp age
            if (temp_rpAge == null && profile.rpAge == 0) {

                try {
                    if (Integer.parseInt(message) >= 15 && Integer.parseInt(message) <= 80) {
                        temp_rpAge = message;
                        awaitResponse_rpAge = true;
                        player.sendMessage(MsgType.STEVE_FULL.getMessage() + "Nan ta pris un si gros coup de vieux que sa? Ta vraiment §6" + temp_rpAge + "ans§r ? §7(Oui/Non)");
                    } else player.sendMessage(MsgType.STEVE_FULL.getMessage() + "§cTrès drôle tiens! Bon aller, arrête ta blague, ta quel âge?");
                } catch (final NumberFormatException ignored) {
                    player.sendMessage(MsgType.STEVE_FULL.getMessage() + "§cEuhhh? Gamin je te demande juste ton âge! Pas des maths avec des lettres!");
                }
                return;
            }

            //Confirm tempRpAge
            if (temp_rpAge != null && awaitResponse_rpAge && profile.rpAge == 0) {

                if (message.contains("oui")) {

                    profile.rpAge = Integer.parseInt(temp_rpAge);
                    profile.isVerified = true;
                    profile.joinDate = TimeUtils.convertMillis(System.currentTimeMillis(), TimeFormat.FULL);
                    player.getInventory().addItem(new ItemStack(Material.IRON_NUGGET));
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 1));
                    IDCardManager.createIDCard(player);
                    PhoneNumber.newNumber(player);
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND));
                    //TODO: Change items to real modded item's id
                    player.sendTitle("§fBienvenue sur §a§lLiberty§2§lCity §4§lRP §6§LV5 §f!", "§7Bonne aventure §a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + "§7.", 3*20, 10*20, 2*20);
                    ChatUtils.broadcast("");
                    ChatUtils.broadcast(MsgType.MAIRIE.getMessage() + "§a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + " §frejoint la ville!");
                    ChatUtils.broadcast("");
                    player.sendMessage(MsgType.STEVE_FULL.getMessage() + "Eh beh. J'me souviens maintenant, on entendais beaucoup parler de toi ya 3 ans, aussi bien chez les flics que les gangs... J'avais réussi a pirater la base de donnée des flics, t'était bien connu de leur coté et pas du bon coté... Fin bref, tu sais depuis que tes partis ça a beaucoup changer. La mairie a refait faire toute la ville, puis ya un §6pôle emplois§r qui a ouvert, sa serait un bon nouveau départ pour toi. Eh! Avant que tu parte, j'ai réussi a te faire une ptite §6pièce d'identité§f, bon oui elle est un peut trafiquer mais garde la elle te sera util. Puis bon temps que on y est, vu que tes une vielle connaissance voila un §6porte feuille§r et §6$20§r. Si ta besoin de moi tu peut m'envoyer un sms, t'aurau juste besoin de faire §6/sms Steve§r.");
                    player.sendMessage(MsgType.MICHEL_FULL.getMessage() + "Et hop la, c'est prét! Un bon gros beef steak pour notre chère... MAIS ATTEND, C'EST TOI §a§l" + profile.rpFirstName + " §2§l" + profile.rpLastName + "§r! Rohhhh sa fait si longtemps que j'avais pas entendu parler de toi! Ravis de te revoir!" );
                    player.sendMessage(MsgType.STEVE_FULL.getMessage() + "Bon aller, prend ton beef §6steak§r, t'en aura surement besoin! A plus!");
                    profile.getProfileThread().execute(() -> FileManager.saveProfile(profile));
                    packet.getEvent().setCancelled(true);

                } else if (message.contains("non")) {

                    shutdown(player);
                    player.sendMessage(MsgType.STEVE_FULL.getMessage() + "§cAh bah tiens j'suis pas le seul a perdre la tête! Bon aller on va reprendre depuis le tous début pour être sur.");
                    player.sendTitle("", "§7§oCommencez par dire votre prénom au barman.", 3*20, 900*20, 2*20);
                }
            }
        }
    }

    public void handle(final ServerPlayPacket packet) {}

    private boolean letterOnly(final String message) {

        for (int i = 0; i < message.length(); i++) {

            if (!Character.isLetter(message.charAt(i))) {

                return false;
            }
        }

        return true;
    }

    public static void initialize(final Player player) {

        player.sendMessage(MsgType.STEVE_HIDDEN.getMessage() + "Ah tiens un client! Bien le bonjour mon vieux! Qu'est ce qui te ferrais plaisir... Eh beh mon vieux, ça va ? Ta pas l'air d'aller bien, ta eu le mal de mer sur le bateau c'est sa? Ahahah je plainsante, MICHEL UN BEEF STEAK STE PLAÎT! Ah attend, tu m'as l'air familier... Tu §6t'appel comment§r par curiositer?");
        player.sendTitle("", "§7§oCommencez par dire votre prénom au barman.", 3*20, 900*20, 2*20);
    }

    public static void shutdown(final Player player) {

        final Profile profile = LibertyCity.getInstance().getProfileManager().getProfile(player.getUniqueId());

        temp_rpAge = null;
        temp_rpFirstName = null;
        temp_rpLastName = null;
        awaitResponse_rpAge = false;
        awaitResponse_rpFirstName = false;
        awaitResponse_rpLastName = false;

        profile.rpAge = 0;
        profile.rpFirstName = null;
        profile.rpLastName = null;
    }

}
