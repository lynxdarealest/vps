package com.beemobi.rongthanonline.npc;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.task.Task;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class NpcTaskController {
    public final Player player;

    public NpcTaskController(Player player) {
        this.player = player;
    }

    public void openMenu(@Nullable Npc npc, int npcId) {
        if (npc == null) {
            return;
        }
        Task task = player.taskMain;
        if (task == null) {
            return;
        }
        List<Command> commands = new ArrayList<>();
        StringBuilder content = new StringBuilder();
        switch (task.template.id) {
            case 0: {
                if (npc.template.id != 0) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Chào mừng con đến với thế giới Rồng Thần Online. Con đã sẵn sàng trừ gian diệt ác, bảo vệ thế giới khỏi những thế lực xấu xa chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1) {
                    npc.chat(player, "Con hãy chạy sang Vách núi Paozu hạ cho ta 10 con người rơm");
                } else if (task.index == 2) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 1: {
                if (npc.template.id != 0) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Sức mạnh tăng nhưng vẫn chưa đủ giúp bản thân con mạnh mẽ hơn, giờ hãy nâng cấp các chỉ số cần thiết và sử dụng các trang bị có trong hành trang của con");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Nhận", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1) {
                    npc.chat(player, "Hãy nâng cấp kĩ năng của con đi");
                } else if (task.index == 2) {
                    npc.chat(player, "Hãy nâng cấp chỉ số tiềm năng của con đi");
                } else if (task.index == 3) {
                    npc.chat(player, "Hãy sử dụng găng tay trong hành trang của con đi");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 2: {
                if (npc.template.id != 1) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Nguyên liệu làm thức ăn của tớ đã hết, tớ cần đến Rừng nấm Fuka để thu thập nguyên liệu. Nhưng trên đường đi có bọn hổ và sói đang cản giữa dường, cậu hãy giúp tớ đánh đuổi bọn chúng?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Nhận", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1 || task.index == 2 || task.index == 3) {
                    npc.chat(player, "Hãy đi làm nhiệm vụ tớ giao");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ tớ giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 3: {
                if (npc.template.id != 1) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Nguồn nước dự trữ của tớ đã hết, tớ cần đến Thác nước Keise để lấy nước. Nhưng ở đó có quái vật, cậu hãy giúp tớ đánh đuổi chứ?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Nhận", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1 || task.index == 2 || task.index == 3) {
                    npc.chat(player, "Hãy đi làm nhiệm vụ tớ giao");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ tớ giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
            }

            case 4: {
                if (npc.template.id == 0) {
                    if (task.index == 0) {
                        content.append("Thế giới Rồng Thần rất là rộng lớn, còn nhiều điều mà con chưa biết đến, hãy đi nói chuyện với Ô Long, Bora và Thần mèo để mở mang kiến thức về thế giới này?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                    } else if (task.index == 1) {
                        npc.chat(player, "Hãy đi làm nhiệm vụ ta giao");
                    } else if (task.index == 2) {
                        npc.chat(player, "Hãy đi làm nhiệm vụ ta giao");
                    } else if (task.index == 3) {
                        npc.chat(player, "Hãy đi làm nhiệm vụ ta giao");
                    } else if (task.index == 4) {
                        content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                    }
                } else if (npc.template.id == 2) {
                    if (task.index == 1) {
                        content.append("Cậu tìm ta có việc gì?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Nói chuyện", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                    }
                } else if (npc.template.id == 5) {
                    if (task.index == 2) {
                        content.append("Cậu tìm ta có việc gì?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Nói chuyện", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                    }
                } else if (npc.template.id == 10) {
                    if (task.index == 3) {
                        content.append("Cậu tìm ta có việc gì?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Nói chuyện", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                    }
                }
                break;
            }

            case 5: {
                if (npc.template.id != 0) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Ta đã đánh rơi viên Ngọc rồng 7 sao, con hãy đi tìm lại cho ta");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1) {
                    npc.chat(player, "Lũ Giran bố rất hung dữ, con cần tập luyện trước");
                } else if (task.index == 2) {
                    npc.chat(player, "Hãy đánh bại Giran bố và lấy lại Ngọc rồng 7 sao giúp ta");
                } else if (task.index == 3) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 6: {
                if (npc.template.id == 0) {
                    if (task.index == 0) {
                        content.append("Ta đã già rồi, giờ t gửi gắm con cho Quy Lão, hãy đi tìm Quy Lão ở Đảo Kamê và theo học võ. Từ giờ t sẽ ở đây trông coi rương đồ cho con?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                    } else if (task.index == 1) {
                        npc.chat(player, "Quy Lão đang đứng chờ con ở Đảo Kamê");
                    } else if (task.index == 2) {
                        content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                    }
                } else if (npc.template.id == 4) {
                    if (task.index == 1) {
                        content.append("Con tìm ta có việc gì?");
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Nói chuyện", player, npcId, 0, npc));
                        commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                    }
                }
                break;
            }

            case 7: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Học trò của ta là phải mạnh mẽ. Đầu tiên, con hãy đi cường hóa các trang bị trong túi đồ. Đây là nhiệm vụ đầu tiên của con?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 8) {
                    npc.chat(player, "Hãy đến gặp Ô Long và cường hóa trang bị của con");
                } else if (task.index == 9) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 8: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Một cây làm chẳng nên non ba cây chụm lại nên hòn núi cao. Con đã sẵn sàng tìm kiếm đồng đội cho mình chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1 || task.index == 2 || task.index == 3 || task.index == 4) {
                    npc.chat(player, "Hãy cùng tổ đội của con tiêu diệt những thế lực xấu xa");
                } else if (task.index == 5) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 9: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Đại vương Pilaf và thuộc hạ của chúng đang săn lùng những viên Ngọc rồng, con hãy đi ngăn chặn chúng lại?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1 || task.index == 2) {
                    npc.chat(player, "Hãy đi tìm và tiêu diệt thuộc hạ của Đại vương Pilaf");
                } else if (task.index == 3) {
                    npc.chat(player, "Hãy đi tìm và Đại vương Pilaf");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 10: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Đạo tặc Yamcha đang lộng hoành cướp bóc ở vùng hoang mạc, con hãy đến đó và hạ hắn?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index < 5) {
                    npc.chat(player, "Hắn rất mạnh, con cần phải tập luyện trước");
                } else if (task.index == 5) {
                    npc.chat(player, "Hãy đi tìm và hạ Yamcha");
                } else if (task.index == 6) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 11: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Đại hội võ thuật là nơi quy tụ những chiến binh mạnh mẽ. Con đã sẵn sàng thể hiện bản thân chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index == 1) {
                    npc.chat(player, "Con cần phải thách đấu thắng 10 chiến binh khác để có thể tham gia đại hội võ thuật");
                } else if (task.index == 2) {
                    npc.chat(player, "Hãy mau đi đăng ký tham gia đại hội võ thuật");
                } else if (task.index == 3) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 12: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Con theo ta cũng đã được 1 thời gian, trang bị của con cũng đã không còn phù hợp nữa. Hãy đi chế tạo những trang bị xứng tầm với con đi?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 9) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 10) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 13: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Vùng đất phía tây có gì đó bất ổn, con hãy đến đó kiểm tra?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 5) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 6) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 14: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Đội quân Fide đã đổ bộ xuống hành tinh Namek, con hãy đến đó và giải cứu người dân Namek");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 5) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 6) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 15: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Lũ đệ tử của Fide đang tàn sát dân làng Namek, con hãy đi ngăn chặn chúng lại");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 3) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 16: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Nhóm sát thủ của Fide đã đến Hành tinh Namek, con hãy đi ngăn chặn chúng lại?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 5) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 6) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 17: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Trang bị của con còn quá yếu, con cần phải nâng cấp trang bị của bản thân lên để có thể chiến đấu với các thế lực tàn ác khác?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 16) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 17) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 18: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Những viên Ngọc rồng Namek đang lưu lạc ở các ngôi làng bên hành tinh Namek, con hãy cùng bang hội thu thập chúng?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 8) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 9) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 19: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Rồng thần Trái đất sẽ thực hiện bất cứ điều gì mà con muốn, con hãy thu thập đủ 7 viên ngọc và triệu hồi Rồng thần");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 4) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 5) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 20: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Fide Đại Ca đang ở hành tinh Namek, hắn là 1 hiểm họa của vũ trụ, con hãy đi tiêu diệt hắn");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 4) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 5) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 21: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Ta có 1 bài kiểm tra năng lực dành cho con, con sẵn sàng chứ?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 4) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 5) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 22: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Bang hội là một phần không thể thiếu để tăng cường sức mạnh, con đã sẵn sàng cho chuỗi thử thách sắp tới chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 2) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 3) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 23: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Bọn Robot sát thủ đã xuất hiện tại khu vực thành phố, con hãy chiến đấu với chúng");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 3) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 24: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Bọn Robot sát thủ đã xuất hiện tại khu vực rừng chết, con hãy chiến đấu với chúng");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 3) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 25: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Bọn Robot sát thủ tiếp tục xuất hiện tại khu vực rừng chết, con hãy chiến đấu với chúng");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 4) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 5) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }

            case 26: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    content.append("Có 1 sinh vật kì lạ xuất hiện tại khu vực rừng chết, con hãy đến đó kiểm tra");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Sẵn sàng", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Từ chối", player, npcId, 1, npc));
                } else if (task.index >= 1 && task.index <= 3) {
                    npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                } else if (task.index == 4) {
                    content.append("Nhiệm vụ ta giao, con đã hoàn thành xong chưa?");
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Hoàn thành", player, npcId, 0, npc));
                    commands.add(new Command(CommandName.CONFIRM_TASK, "Đóng", player, npcId, 1, npc));
                }
                break;
            }
        }
        if (!commands.isEmpty()) {
            player.createMenu(npcId, content.toString(), commands);
        }
    }

    public void confirmMenu(@Nullable Npc npc, int npcId, int select) {
        if (npc == null) {
            return;
        }
        Task task = player.taskMain;
        if (task == null) {
            return;
        }
        switch (task.template.id) {
            case 0: {
                if (npc.template.id != 0) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Tốt lắm, con hãy sang Vách núi Paozu hạ cho ta 5 con người rơm");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 2) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta sẽ giúp con tăng 1 chút sức mạnh");
                        player.upPower(100);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 100 sức mạnh");
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 1: {
                if (npc.template.id != 0) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Tốt lắm, hãy nâng cấp chỉ số đi");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 4) {
                    if (select == 0) {
                        npc.chat(player, "Hành trình phía trước còn dài...");
                        player.upXuKhoa(10000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau làm nhiệm vụ đi rồi về gặp ta");
                    }
                }
                break;
            }
            case 2: {
                if (npc.template.id != 1) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        if (player.getCountItemBagEmpty() < 2) {
                            player.addInfo(Player.INFO_RED, "Cần ít nhất 2 ô trống trong túi đồ");
                        } else {
                            player.addItem(ItemManager.getInstance().createItem(ItemName.COM_NAM, 2, true));
                            player.nextTaskIndex();
                            npc.chat(player, "Hãy ăn cơm nắm ở hành trang, nó sẽ giúp cậu phục hồi thể lực theo thời gian");
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Cậu còn non và xanh lắm!");
                    }
                } else if (task.index == 4) {
                    npc.chat(player, "Cám ơn cậu rất nhiều. Từ giờ muốn mua gì hãy đến gặp tớ, tớ sẽ giảm giá cho cậu");
                    player.upPower(1000);
                    player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 1000 sức mạnh");
                    player.upXuKhoa(10000);
                    player.nextTask();
                }
                break;
            }

            case 3: {
                if (npc.template.id != 1) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Lũ quái vật rất hung dữ, cậu cần phải tập luyện trước!");
                    } else if (select == 1) {
                        npc.chat(player, "Cậu thật là kém cỏi!");
                    }
                } else if (task.index == 4) {
                    if (select == 0) {
                        if (player.getCountItemBagEmpty() < 1) {
                            player.addInfo(Player.INFO_RED, "Cần ít nhất 1 ô trống trong túi đồ");
                        } else {
                            player.addItem(ItemManager.getInstance().createItem(ItemName.CAPSULE_TAU_BAY_THUONG, 30, true));
                            player.upPower(1000);
                            player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 1000 sức mạnh");
                            player.upXuKhoa(100000);
                            player.nextTask();
                            player.clearItemTask();
                            npc.chat(player, "Good!!! Từ giờ sẽ giảm giá mọi mặt hàng cho cậu, cần gì hãy đến gặp tớ");
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ đi rồi về báo cáo cho tớ!");
                    }
                }
                break;
            }

            case 4: {
                if (npc.template.id == 0) {
                    if (task.index == 0) {
                        if (select == 0) {
                            npc.chat(player, "Tốt lắm, ta tự hào về con");
                            player.nextTaskIndex();
                        } else if (select == 1) {
                            npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                        }
                    } else if (task.index == 4) {
                        if (select == 0) {
                            npc.chat(player, "Tốt lắm, ta tự hào về con");
                            player.upPower(15000);
                            player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 15000 sức mạnh");
                            player.upXuKhoa(100000);
                            player.nextTask();
                        } else if (select == 1) {
                            npc.chat(player, "Ta thất vọng về con!");
                        }
                    }
                }
                if (npc.template.id == 2 && task.index == 1) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Ta đứng đây và luôn sẵn lòng cường hóa vật phẩm cho cậu, vật phẩm sau khi được cường hóa sẽ ban cho cậu sức mạnh");
                    }
                }
                if (npc.template.id == 5 && task.index == 2) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Ta sẽ giúp cậu lên Thần điện");
                    }
                }
                if (npc.template.id == 10 && task.index == 3) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Khi nào cần đậu thần, hãy đến tìm ta");
                    }
                }
                break;
            }

            case 5: {
                if (npc.template.id != 0) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Bọn Giran bố rất mạnh, con cần phải tập luyện trước");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 3) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta sẽ giúp con tăng 1 chút sức mạnh");
                        player.upPower(20000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 20000 sức mạnh");
                        player.upXuKhoa(100000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }

            case 6: {
                if (npc.template.id == 0) {
                    if (task.index == 0) {
                        if (select == 0) {
                            player.nextTaskIndex();
                            npc.chat(player, "Hãy đến Đảo Kamê và nói chuyện với Quy Lão");
                        } else if (select == 1) {
                            npc.chat(player, "Ta thất vọng về con!");
                        }
                    } else if (task.index == 2) {
                        if (select == 0) {
                            if (player.getIndexItemBagEmpty() == -1) {
                                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                            } else {
                                npc.chat(player, "Tốt lắm, hãy theo Quy Lão học hành chăm chỉ, ta luôn tự hào về con");
                                player.upPower(20000);
                                player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 20000 sức mạnh");
                                player.addItem(ItemManager.getInstance().createItem(ItemName.DA_1, 100, true), true);
                                player.nextTask();
                            }
                        } else if (select == 1) {
                            npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                        }
                    }
                }
                if (npc.template.id == 4 && task.index == 1) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Ta sẽ nhận con làm đệ tử");
                    }
                }
                break;
            }
            case 7: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi cường hóa các trang bị");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 9) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, hành trình giờ mới bắt đầu");
                        player.upPower(100000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 100000 sức mạnh");
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 8: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy tìm kiếm tổ đội và hoàn thành nhiệm vụ ta giao");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 5) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, con đã mạnh mẽ hơn rất nhiều rồi");
                        player.upPower(500000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 500000 sức mạnh");
                        player.upXuKhoa(1000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 9: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi tiêu diệt Đại vương Pilaf");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 4) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, không hổ danh là đệ tử của ta");
                        player.upPower(500000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 500000 sức mạnh");
                        player.upXuKhoa(1000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 10: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Yamcha rất mạnh, con cần phải tập luyện trước");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 6) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(1000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 1000000 sức mạnh");
                        player.upXuKhoa(1000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 11: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi thách đấu 10 người chơi khác");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 3) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(1000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 1000000 sức mạnh");
                        player.upXuKhoa(1000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 12: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 10) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(5000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 5000000 sức mạnh");
                        player.upXuKhoa(5000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 13: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 6) {
                    if (select == 0) {
                        npc.chat(player, "Con đã nhớ lại mọi thứ? Con sẽ làm gì?");
                        player.upPower(5000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 5000000 sức mạnh");
                        player.upXuKhoa(5000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 14: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy đến gặp Tiến sĩ Brief, ông ấy sẽ đưa con đến hành tinh Namek");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 6) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(5000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 5000000 sức mạnh");
                        player.upXuKhoa(5000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 15: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 4) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(5000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 5000000 sức mạnh");
                        player.upXuKhoa(5000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 16: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 6) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(50000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                        player.upXuKhoa(10000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 17: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 17) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(50000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                        player.upXuKhoa(10000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 18: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        if (player.getIndexItemBagEmpty() == -1) {
                            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        } else {
                            player.nextTaskIndex();
                            npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                            player.addItem(ItemManager.getInstance().createItem(ItemName.RADAR_RT, 10, true));
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 9) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(50000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                        player.upXuKhoa(10000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 19: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        if (player.getIndexItemBagEmpty() == -1) {
                            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        } else {
                            player.nextTaskIndex();
                            npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                            player.addItem(ItemManager.getInstance().createItem(ItemName.NGOC_RONG_2_SAO, 1, true));
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 5) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(50000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                        player.upXuKhoa(10000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
            case 20: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        if (player.getIndexItemBagEmpty() == -1) {
                            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        } else {
                            player.nextTaskIndex();
                            npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                            player.addItem(ItemManager.getInstance().createItem(ItemName.RADAR_RT2, 10, true));
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 5) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(50000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                        player.upXuKhoa(10000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }

            case 21: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 5) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(50000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                        player.upXuKhoa(10000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }

            case 22: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 3) {
                    if (select == 0) {
                        npc.chat(player, "Tốt lắm, ta tự hào về con");
                        player.upPower(50000000);
                        player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                        player.upXuKhoa(10000000);
                        player.nextTask();
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }

            case 23: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 4) {
                    if (select == 0) {
                        if (player.isBagFull()) {
                            npc.chat(player, "Cần ít nhất 1 ô trống trong túi đồ");
                        } else {
                            npc.chat(player, "Tốt lắm, ta tự hào về con");
                            player.upPower(50000000);
                            player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                            player.upXuKhoa(10000000);
                            player.nextTask();
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }

            case 24: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 4) {
                    if (select == 0) {
                        if (player.isBagFull()) {
                            npc.chat(player, "Cần ít nhất 1 ô trống trong túi đồ");
                        } else {
                            npc.chat(player, "Tốt lắm, ta tự hào về con");
                            player.nextTask();
                            player.upPower(50000000);
                            player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                            player.upXuKhoa(10000000);
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }

            case 25: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 5) {
                    if (select == 0) {
                        if (player.isBagFull()) {
                            npc.chat(player, "Cần ít nhất 1 ô trống trong túi đồ");
                        } else {
                            npc.chat(player, "Tốt lắm, ta tự hào về con");
                            player.upPower(50000000);
                            player.addInfo(Player.INFO_YELLOW, "Bạn được thưởng 50000000 sức mạnh");
                            player.upXuKhoa(10000000);
                            player.nextTask();
                        }
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }

            case 26: {
                if (npc.template.id != 4) {
                    break;
                }
                if (task.index == 0) {
                    if (select == 0) {
                        player.nextTaskIndex();
                        npc.chat(player, "Hãy mau đi làm nhiệm vụ rồi về gặp ta");
                    } else if (select == 1) {
                        npc.chat(player, "Ta thất vọng về con!");
                    }
                } else if (task.index == 4) {
                    if (select == 0) {
                        npc.chat(player, "Hiện tại chưa có nhiệm vụ mới");
                        /*if (player.isBagFull()) {
                            npc.chat(player, "Cần ít nhất 1 ô trống trong túi đồ");
                        } else {
                            npc.chat(player, "Tốt lắm, ta tự hào về con");
                            player.nextTask();
                            player.addItemToBag(ItemManager.getInstance().createItem(ItemName.NGOC_RONG_2_SAO, 2, true), true);
                        }*/
                    } else if (select == 1) {
                        npc.chat(player, "Mau đi làm nhiệm vụ rồi về gặp ta");
                    }
                }
                break;
            }
        }
    }


}
