package com.beemobi.rongthanonline.npc;

import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.map.expansion.congress.MartialCongress;
import com.beemobi.rongthanonline.map.expansion.festival.MartialArtsFestival;
import com.beemobi.rongthanonline.map.expansion.survival.Survival;
import com.beemobi.rongthanonline.map.expansion.tournament.Tournament;
import com.beemobi.rongthanonline.map.expansion.tournament.ZoneTournament;
import com.beemobi.rongthanonline.map.expansion.treasure.Treasure;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.upgrade.UpgradeType;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class NpcController {
    public final Player player;

    public NpcController(Player player) {
        this.player = player;
    }

    public void openMenu(int npcId, Npc npc) {
        List<Command> commands = new ArrayList<>();
        StringBuilder content = new StringBuilder();
        if (npc != null && npc.isTask(player)) {
            commands.add(new Command(CommandName.SHOW_TASK, "Nhiệm vụ", player, npcId, npc));
        }
        switch (npcId) {
            case NpcName.ONG_GOHAN: {
                if (npc != null) {
                    if (npc.zone.map.template.id == 0) {
                        content.append("Con tìm ta có việc gì?");
                        commands.add(new Command(CommandName.SHOW_TOP, "Bảng xếp hạng", player));
                        commands.add(new Command(CommandName.VAO_NHA, "Vào nhà", player));
                    } else {
                        commands.add(new Command(CommandName.ROI_NHA, "Rời đi", player));
                    }
                }
                break;
            }

            case NpcName.BA_HAT_MIT: {
                if (npc != null) {
                    content.append("Ngươi tìm ta có việc gì?");
                    if (npc.zone.map.template.id == 0) {
                        commands.add(new Command(CommandName.SHOW_SHOP_BA_HAT_MIT, "Cửa hàng", player, npc));
                    } else {
                        commands.add(new Command(CommandName.CRYSTALLIZE, "Pha lê hóa", player));
                        commands.add(new Command(CommandName.POLISH, "Đánh bóng Pha lê", player));
                        commands.add(new Command(CommandName.ENCHANT_ITEM, "Ép Pha lê", player));
                        commands.add(new Command(CommandName.UP_STAR_PET, "Nâng sao Pet", player));
                        commands.add(new Command(CommandName.COSTUME_MERGING, "Ghép\ntrang bị", player));
                        commands.add(new Command(CommandName.OPEN_UPGRADE, "Ghép\nHuy hiệu\nĐại gia", player, UpgradeType.GHEP_HUY_HIEU_DAI_GIA));
                        commands.add(new Command(CommandName.COMBINE, "Nhập\nNgọc rồng", player));
                    }
                }
                break;
            }

            case NpcName.BUNMA: {
                if (npc != null) {
                    content.append("Cậu tìm tớ có việc gì?");
                    commands.add(new Command(CommandName.SHOW_SHOP_BUNMA, "Cửa hàng", player, npc));
                    commands.add(new Command(CommandName.SHOP_SALE, "Black Friday", player, npc));
                    commands.add(new Command(CommandName.CALL_DRAGON, "Rồng thần", player, Server.getInstance().dragon));
                }
                break;
            }

            case NpcName.O_LONG: {
                if (npc != null) {
                    content.append("Cậu tìm ta có việc gì?");
                    commands.add(new Command(CommandName.UPGRADE_ITEM, "Cường hóa", player));
                    commands.add(new Command(CommandName.UPGRADE_STONE, "Luyện đá", player));
                    commands.add(new Command(CommandName.SHOW_EQUIP_CRAFTING, "Chế tạo\ntrang bị", player, npc));
                    commands.add(new Command(CommandName.SHOW_TAI_CHE, "Tái chế\ntrang bị", player));
                }
                break;
            }

            case NpcName.CHI_CHI: {
                if (npc != null) {
                    if (npc.zone.map.template.id == MapName.LANH_DIA_BANG_HOI) {
                        content.append("Cậu tìm ta có việc gì?");
                        Clan clan = player.clan;
                        if (clan != null) {
                            commands.add(new Command(CommandName.CLAN_SHOP, "Cửa hàng", player));
                            commands.add(new Command(CommandName.SHOW_TASK_CLAN, "Nhiệm vụ", player));
                            if (clan.exp >= clan.getMaxExp()) {
                                commands.add(new Command(CommandName.UPGRADE_CLAN, "Nâng cấp", player));
                            } else {
                                commands.add(new Command(CommandName.HUONG_DAN_NANG_CAP_BANG, "Hướng dẫn nâng cấp", player));
                            }
                            commands.add(new Command(CommandName.SHOW_MENU_VUNG_DAT_BI_AN, "Vùng đất bí ẩn", player));
                        }
                        commands.add(new Command(CommandName.LANH_DIA_BANG_TO_TTVT_TRAI_DAT, "Trạm tàu vũ trụ", player));
                    } else if (npc.zone.map.template.id == MapName.TRAM_TAU_VU_TRU) {
                        content.append("Cậu tìm ta có việc gì?");
                        if (player.clan == null) {
                            commands.add(new Command(CommandName.CREATE_CLAN, "Tạo bang", player));
                        } else {
                            commands.add(new Command(CommandName.LANH_DIA_BANG_HOI, "Lãnh địa bang hội", player));
                        }
                    } else {
                        content.append("Chúng quá mạnh, cậu có muốn phù hộ không?\n Phù hộ sẽ +100% sức mạnh và 50% giảm sát thương từ quái");
                        commands.add(new Command(CommandName.PHU_HO_LANH_DIA, "Phù hộ\n10 KC", player));
                        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                    }
                }
                break;
            }

            case NpcName.TIEN_SI_BRIEF: {
                if (npc != null) {
                    content.append("Tàu của ta có thể đưa cậu đi bất cứ nơi đâu");
                    commands.add(new Command(CommandName.EARTH_TO_NAMEK, "Đến Namek", player));
                }
                break;
            }

            case NpcName.KAR_DO: {
                if (npc != null) {
                    content.append("Tàu của tôi còn lạc hậu những vẫn có thể đưa cậu đi bất cứ nơi đâu");
                    commands.add(new Command(CommandName.CHIEN_TRUONG_NGOC_RONG_NAMEK, "Ngọc rồng\nNamek", player));
                    commands.add(new Command(CommandName.NAMEK_TO_EARTH, "Đến\nTrái đất", player));
                }
                break;
            }

            case NpcName.DOI_TRUONG_VANG: {
                if (npc != null) {
                    content.append("Hãy tìm những đồng đội ngang tài ngang sức để cùng nhau chinh phục Bản doanh Red").append("\n");
                    content.append("Nhiều phần thưởng hấp dẫn đang chờ ngươi");
                    commands.add(new Command(CommandName.OPEN_BARRACK, "Tham gia", player));
                    commands.add(new Command(CommandName.HUONG_DAN_BAN_DOANH_RED, "Hướng dẫn", player));
                    commands.add(new Command(CommandName.SHOW_SHOP_BARRACK, "Cửa hàng", player));
                }
                break;
            }

            case NpcName.THAN_MEO: {
                if (npc != null) {
                    content.append("Ngươi tìm ta có việc gì?");
                    commands.add(new Command(CommandName.THAP_KARIN_TO_RUNG_KARIN, "Xuống Thánh địa", player));
                    commands.add(new Command(CommandName.GO_TO_THAN_DIEN, "Lên Thần điện", player));
                    commands.add(new Command(CommandName.SHOW_MAGIC_BEAN, "Đậu thần", player, npc));
                }
                break;
            }

            case NpcName.THUONG_DE:
            case NpcName.POPO: {
                if (npc != null) {
                    content.append("Cậu tìm ta có việc gì?");
                    commands.add(new Command(CommandName.TRAIN_OFFLINE, "Luyện tập\nOffline", player));
                    commands.add(new Command(CommandName.VE_THAP_KARIN, "Xuống Tháp Karin", player));
                }
                break;
            }

            case NpcName.BO_MONG: {
                if (npc != null) {
                    content.append("Cậu tìm ta có việc gì?");
                    commands.add(new Command(CommandName.RUNG_KARIN_TO_THAP_KARIN, "Lên Tháp Karin", player));
                    commands.add(new Command(CommandName.SHOW_MENU_TASK_DAILY, "Nhiệm vụ hàng ngày", player));
                    commands.add(new Command(CommandName.SHOW_ACHIEVEMENT, "Thành tựu", player));
                }
                break;
            }

            case NpcName.TRUONG_LAO: {
                if (npc != null) {
                    content.append("Con tìm ta có việc gì?");
                    commands.add(new Command(CommandName.OPEN_LIMIT_POWER, "Mở\ngiới hạn\nsức mạnh", player, npc));
                    commands.add(new Command(CommandName.SHOW_LIST_UPGRADE_SKILL, "Cường hóa\nkỹ năng", player, npc));
                    commands.add(new Command(CommandName.TTVT_NAMEK_TO_LANG_CO_GIRA, "Đến\nLàng cổ\nGira", player, npc));
                }
                break;
            }

            case NpcName.PU: {
                if (npc != null) {
                    if (npc.zone.map.template.id == MapName.CUA_KHONG_GIAN) {
                        Survival survival = MapManager.getInstance().survival;
                        if (survival != null) {
                            content.append(String.format("Hiện tại đã có %d người tham gia", survival.getGamers().size())).append("\n");
                            long[] rewards = survival.rewards;
                            if (rewards[0] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 1: %s xu", Utils.formatNumber(rewards[0]))).append("\n");
                            }
                            if (rewards[1] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 2: %s xu", Utils.formatNumber(rewards[1]))).append("\n");
                            }
                            if (rewards[2] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 3: %s xu", Utils.formatNumber(rewards[2]))).append("\n");
                            }
                            if (rewards[3] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 4-10: %s xu", Utils.formatNumber(rewards[3]))).append("\n");
                            }
                            if (!survival.donors.isEmpty()) {
                                if (survival.donors.size() == 1) {
                                    content.append("Nhà tài trợ: ");
                                } else {
                                    content.append("Các tài trợ: ");
                                }
                                int num = 0;
                                for (String name : survival.donors.keySet()) {
                                    content.append(String.format("%s (%s)", name, Utils.formatNumber(survival.donors.get(name))));
                                    if (num < survival.donors.size() - 1) {
                                        content.append(", ");
                                    }
                                    num++;
                                }
                                content.append("\n");
                            }
                            content.append("Trận chiến sẽ diễn ra sau vài phút nữa");
                            commands.add(new Command(CommandName.HUONG_DAN_SINH_TON, "Thông tin", player));
                            commands.add(new Command(CommandName.TRAO_THUONG_SINH_TON, "Trao thưởng", player));
                        }
                        commands.add(new Command(CommandName.CUA_KHONG_GIAN_TO_TTVT_TRAI_DAT, "Quay về", player, npc));
                    } else if (npc.zone.map.template.id == MapName.TRAM_TAU_VU_TRU) {
                        content.append("Ngươi tìm ta có việc gì?");
                        commands.add(new Command(CommandName.THANH_PHO_LANG_QUEN, "Thành phố\nLãng quên", player, npc));
                        commands.add(new Command(CommandName.SINH_TON, "Chiến trường\nsinh tồn", player, npc));
                        commands.add(new Command(CommandName.FLAG_WAR, "Cướp cờ", player, -1));
                        commands.add(new Command(CommandName.TELEPORT_DAO_BANG_HOA, String.format("Đảo\nbăng hỏa\n%s xu", Utils.formatNumber(500000L + 100000L * Math.max(player.level - 30, 0))), player, npc));
                    } else if (npc.zone.map.template.id == MapName.HANH_TINH_NGUC_TU) {
                        content.append("Ai sợ thì đi về?");
                        commands.add(new Command(CommandName.FLAG_WAR, "Cắm cờ", player, 3));
                        commands.add(new Command(CommandName.FLAG_WAR, "Bảng xếp hạng", player, 4));
                        commands.add(new Command(CommandName.LANH_DIA_BANG_TO_TTVT_TRAI_DAT, "Quay về", player, npc));
                    }
                }
                break;
            }

            case NpcName.SAN_TA: {
                if (npc != null) {
                    if (npc.zone.map.template.id == MapName.DAI_HOI_VO_THUAT) {
                        MartialArtsFestival festival = MapManager.getInstance().martialArtsFestival;
                        if (!MartialArtsFestival.histories.isEmpty()) {
                            commands.add(new Command(CommandName.HISTORY_DHVT, "Lịch sử trận đấu", player, npc));
                        }
                        if (festival == null) {
                            content.append("Hiện tại chưa có giải đấu nào diễn ra");
                            commands.add(new Command(CommandName.CREATE_DAI_HOI_VO_THUAT, "Tạo giải đấu", player, npc));
                            commands.add(new Command(CommandName.BXH_DHVT, "Bảng xếp hạng", player, npc));
                            commands.add(new Command(CommandName.THONG_TIN_DAI_HOI_VO_THUAT, "Thông tin", player, npc));
                        } else {
                            content.append(String.format("Giải đấu %s đang diễn ra", festival.name)).append("\n");
                            long[] rewards = festival.rewards;
                            if (rewards[0] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 1: %s xu", Utils.formatNumber(rewards[0]))).append("\n");
                            }
                            if (rewards[1] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 2: %s xu", Utils.formatNumber(rewards[1]))).append("\n");
                            }
                            if (rewards[2] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 3: %s xu", Utils.formatNumber(rewards[2]))).append("\n");
                            }
                            if (rewards[3] > 0) {
                                content.append(String.format("Phần thưởng thêm cho top 4-10: %s xu", Utils.formatNumber(rewards[3]))).append("\n");
                            }
                            if (!festival.donors.isEmpty()) {
                                if (festival.donors.size() == 1) {
                                    content.append("Nhà tài trợ: ");
                                } else {
                                    content.append("Các tài trợ: ");
                                }
                                int num = 0;
                                for (String name : festival.donors.keySet()) {
                                    content.append(String.format("%s (%s)", name, Utils.formatNumber(festival.donors.get(name))));
                                    if (num < festival.donors.size() - 1) {
                                        content.append(", ");
                                    }
                                    num++;
                                }
                                content.append("\n");
                            }
                            commands.add(new Command(CommandName.TRAO_THUONG_DHVT, "Treo thưởng", player, npc));
                            if (festival.state == ExpansionState.WAIT_REG) {
                                long time = MartialArtsFestival.MINUTE_WAIT_REG * 60000L + festival.startTime - System.currentTimeMillis();
                                if (time > 0) {
                                    content.append(String.format("Hiện tại đã có %d đăng ký", festival.getWarriors().size())).append("\n");
                                    content.append(String.format("Còn %s để đăng ký", Utils.formatTime(time)));
                                    commands.add(new Command(CommandName.DANG_KY_DAI_HOI_VO_THUAT, "Đăng ký" + (festival.frees > 0 ? ("\n" + Utils.formatNumber(festival.frees) + " xu") : ""), player, npc));
                                } else {
                                    content.append("Đã hết thời gian đăng ký");
                                    commands.add(new Command(CommandName.CANCEL, "Đóng", player, npc));
                                }
                            } else {
                                content.append("Đã hết thời gian đăng ký");
                                commands.add(new Command(CommandName.CANCEL, "Đóng", player, npc));
                            }
                        }
                    }
                }
                break;
            }

            case NpcName.TRUONG_LAO_1_SAO:
            case NpcName.TRUONG_LAO_2_SAO:
            case NpcName.TRUONG_LAO_3_SAO:
            case NpcName.TRUONG_LAO_4_SAO:
            case NpcName.TRUONG_LAO_5_SAO:
            case NpcName.TRUONG_LAO_6_SAO:
            case NpcName.TRUONG_LAO_7_SAO: {
                if (npc != null) {
                    content.append("Hay đem viên Ngọc Rồng về cho bang hội để nhận những phần thường quý giá");
                    commands.add(new Command(CommandName.SHOW_MENU_PHU_HO, "Phù hộ", player));
                    commands.add(new Command(CommandName.EARTH_TO_NAMEK, "Quay về", player));
                }
                break;
            }

            case NpcName.TOWA:
            case NpcName.HANG_NGA:
            case NpcName.CU_DO:
            case NpcName.VUA_HUNG: {
                if (npc != null) {
                    if (Event.isEvent()) {
                        content.append("Ngươi tìm ta có việc gì?");
                        commands.add(new Command(CommandName.SHOW_EVENT, "Sự kiện", player, -1));
                    }
                }
                break;
            }

            case NpcName.BEE_CONSIGNMENT: {
                if (npc != null) {
                    content.append("Cậu tìm ta có việc gì?");
                    commands.add(new Command(CommandName.BEE_CONSIGNMENT, "Kí gửi", player));
                    commands.add(new Command(CommandName.HUONG_DAN_BEE_CONSIGNMENT, "Hướng dẫn", player));
                }
                break;
            }

            case NpcName.QUY_LAO: {
                if (npc != null) {
                    if (npc.zone.map.template.id == MapName.DAO_KAME) {
                        content.append("Con tìm ta có việc gì?");
                        commands.add(new Command(CommandName.THAM_GIA_DONG_KHO_BAU, "Động kho báu", player, npc));
                        if (!Server.getInstance().isInterServer()) {
                            commands.add(new Command(CommandName.VAO_MAP_TOURNAMENT, "Thiên bảng", player));
                            commands.add(new Command(CommandName.DAU_TRUONG_CELL, "Đấu trường\nCell", player));
                            commands.add(new Command(CommandName.MENU_LOI_DAI, "Đấu trường\nSinh tử", player));
                        }
                    } else if (npc.zone.map.template.id == MapName.DAU_TRUONG) {
                        if (npc.zone instanceof ZoneTournament) {
                            content.append("Chào mừng bạn đến với Thiên bảng").append("\n");
                            content.append("Giải đấu được tổ chức 24/7");
                            commands.clear();
                            Tournament tournament = MapManager.getInstance().tournament;
                            if (tournament != null) {
                                commands.add(new Command(CommandName.SHOW_LIST_TOURNAMENT_ATHLETE, "Danh sách\nthi đấu", player));
                                commands.add(new Command(CommandName.PHAN_THUONG_TOURNAMENT, "Phần thưởng", player));
                            }
                        } else {
                            content.append("Chào mừng bạn đến với Đấu trường Cell").append("\n");
                            content.append("Giải đấu được tổ chức 24/7");
                            commands.clear();
                            MartialCongress martialCongress = MapManager.getInstance().martialCongress;
                            if (martialCongress != null) {
                                int level = martialCongress.getLevel(player);
                                if (level > 0) {
                                    commands.add(new Command(CommandName.NHAN_THUONG_DAU_TRUONG, String.format(String.format("Nhận thưởng\nCapsule [+%d]", level)), player));
                                }
                                int count = martialCongress.getCount(player);
                                if (count < MartialCongress.MAX_LEVEL) {
                                    long coin = 500_000L * (count + 1);
                                    commands.add(new Command(CommandName.DANG_KY_THI_DAU_DAU_TRUONG, String.format("Đăng ký\n%s xu", Utils.formatNumber(coin)), player));
                                }
                            }
                        }
                        commands.add(new Command(CommandName.VE_DAO_KAME, "Về Đảo Kamê", player));
                    } else if (npc.zone.map.isTreasure()) {
                        commands.add(new Command(CommandName.HUONG_DAN_DONG_KHO_BAU, "Hướng dẫn", player));
                        Treasure treasure = MapManager.getInstance().treasure;
                        if (treasure != null) {
                            int index = player.typeFlag == Treasure.FLAGS[0] ? 0 : 1;
                            content.append(String.format("Hiện tại con đang theo phe %s (%d chiến binh)", index == 0 ? "Đỏ" : "Xanh", treasure.getTotalPirate(index))).append("\n");
                            content.append(String.format("Tổng điểm hiện tại của phe con là %d", treasure.getTotalPoint(index))).append("\n");
                            content.append(String.format("Tổng điểm hiện tại của phe đối thủ là %d", treasure.getTotalPoint(index == 0 ? 1 : 0))).append("\n");
                            content.append(String.format("Thành tích hiện tại của con là %d (Tổng %d)", treasure.getPointPirate(player.id), treasure.getTotalPointPirate(player.id)));
                            if (treasure.state == ExpansionState.WAIT_REG) {
                                long now = System.currentTimeMillis();
                                long time = Treasure.MINUTE_WAIT_REG * 60000L + treasure.startTime - now;
                                if (time > 0) {
                                    content.append("\n");
                                    content.append(String.format("Trận đấu sẽ diễn ra sau %s", Utils.formatTime(time)));
                                }
                            } else {
                                commands.add(new Command(CommandName.BANG_XEP_HANG_DONG_KHO_BAU, "Bảng xếp hạng", player));
                            }
                            commands.add(new Command(CommandName.TREASURE_TO_DAO_KAME, "Quay về", player));
                        }
                    }
                }
                break;
            }

            case NpcName.SONGOKU: {
                if (npc != null) {
                    content.append("Cậu tìm ta có việc gì?");
                    if (npc.zone.map.template.id == MapName.THUNG_LUNG_TURI) {
                        commands.add(new Command(CommandName.TELEPORT_HANH_TINH_YARDRAT, "Đến hành tinh Yardrat", player, npc));
                    } else {
                        commands.add(new Command(CommandName.TELEPORT_THUNG_LUNG_TURI, "Về Thung lũng Turi", player, npc));
                        commands.add(new Command(CommandName.CUA_HANG_YARDRAT, "Cửa hàng", player, npc));
                    }
                }
                break;
            }

            case NpcName.THIEN_MOC_MIN:
            case NpcName.THIEN_MOC_MAX:
            case NpcName.HAC_MOC_MIN:
            case NpcName.HAC_MOC_MAX:
            case NpcName.CAY_THONG_MIN:
            case NpcName.CAY_THONG_MAX:
            case NpcName.CAY_DAO_MIN:
            case NpcName.CAY_DAO_MAX:
            case NpcName.CAY_NAM_MIN:
            case NpcName.CAY_NAM_MAX:
            case NpcName.CAY_CHUOI_MIN:
            case NpcName.CAY_CHUOI_MAX:
            case NpcName.CAY_DUA_MIN:
            case NpcName.CAY_DUA_MAX: {
                if (npc != null) {
                    content.append(npc.template.name);
                    NpcTree npcTree = (NpcTree) npc;
                    commands.add(new Command(CommandName.THU_HOACH_QUA, "Thu hoạch" + (npcTree.time > 0 ? String.format("\n%d phút", npcTree.time) : ""), player, npc));
                }
                break;
            }

            case NpcName.TO_SU_KAIO: {
                if (npc != null) {
                    content.append("Con tìm ta có việc gì?");
                    commands.add(new Command(CommandName.CUA_HANG_HIEP_SI, "Cửa hàng", player, npc));
                    commands.add(new Command(CommandName.CHE_TAO_TRANG_BI_RIDER, "Chế tạo\ntrang bị\nhiệp sĩ", player, npc));
                    commands.add(new Command(CommandName.DOI_CHI_SO_HIEP_SI, "Đổi chỉ số\ntrang bị\nhiệp sĩ", player, npc));
                    commands.add(new Command(CommandName.UPGRADE_ITEM_RIDER, "Nâng cấp\ntrang bị\nhiệp sĩ", player, npc));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Nâng cấp\ntrang bị\nPet", player, UpgradeType.NANG_TRANG_BI_PET));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Đổi\ntrang bị\nPet", player, UpgradeType.DOI_TRANG_BI_PET));
                }
                break;
            }

            case NpcName.KAIOSHIN: {
                if (npc != null) {
                    if (npc.zone.map.template.id == MapName.PHIA_BAC_HANH_TINH_BILL) {
                        commands.add(new Command(CommandName.TELEPORT_DAO_KAME, "Về Đảo Kamê", player, npc));
                        commands.add(new Command(CommandName.CANCEL, "Từ chối", player, npc));
                    } else if (npc.zone.map.template.id != MapName.THANH_DIA) {
                        content.append("Vào lúc 12h30 và 14h sẽ diễn ra đại chiến Mabư với nhiều phần thưởng hấp dẫn, cậu có muốn tham gia không?");
                        commands.add(new Command(CommandName.DEN_HANH_TINH_BILL, "Đến Hành tinh Bill", player, npc));
                        commands.add(new Command(CommandName.THAM_GIA_THANH_DIA, "Tham gia", player, npc));
                        commands.add(new Command(CommandName.CUA_HANG_SPACESHIP, "Đổi điểm", player, npc));
                        commands.add(new Command(CommandName.CANCEL, "Từ chối", player, npc));
                    } else {
                        content.append("Mabư quá mạnh?");
                        commands.add(new Command(CommandName.VE_DAO_KAME, "Về Đảo Kamê", player, npc));
                        commands.add(new Command(CommandName.CANCEL, "Từ chối", player, npc));
                    }
                }
                break;
            }

            case NpcName.BABIDI: {
                if (npc != null) {
                    content.append("Mau hiến dâng sức mạnh cho ta");
                    if (npc.zone.map.template.id != MapName.SPACESHIP_4) {
                        commands.add(new Command(CommandName.XUONG_TANG_SPACESHIP, "Xuồng tầng", player, npc));
                    }
                    commands.add(new Command(CommandName.VE_DAO_KAME, "Về Đảo Kamê", player, npc));
                    if (!player.isHaveEffect(EffectName.PHU_HO_MABU)) {
                        commands.add(new Command(CommandName.PHU_HO_MABU, "Phù hộ\n5KC", player, npc));
                    }
                }
                break;
            }

            case NpcName.WHIS: {
                if (npc != null) {
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Ghép\nHuy hiệu", player, UpgradeType.GHEP_HUY_HIEU));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Ghép\nCải trang", player, UpgradeType.GHEP_CAI_TRANG));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Ghép\nThú cưỡi", player, UpgradeType.GHEP_THU_CUOI));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Ghép\nAura", player, UpgradeType.GHEP_AURA));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Ghép\nVật phẩm\nĐeo lưng", player, UpgradeType.GHEP_DEO_LUNG));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Tách\nHuy hiệu", player, UpgradeType.TACH_HUY_HIEU));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Tách\nCải trang", player, UpgradeType.TACH_CAI_TRANG));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Tách\nThú cưỡi", player, UpgradeType.TACH_THU_CUOI));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Tách\nAura", player, UpgradeType.TACH_AURA));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Tách\nVật phẩm\nĐeo lưng", player, UpgradeType.TACH_DEO_LUNG));
                    commands.add(new Command(CommandName.CRAFTING_PORATA, "Nâng cấp\nBông tai", player, npc));
                    commands.add(new Command(CommandName.UPGRADE_PORATA, "Cường hóa\nBông tai", player, npc));
                    commands.add(new Command(CommandName.OPEN_OPTION_PORATA, "Mở\nchỉ số\nBông tai", player, npc));
                    commands.add(new Command(CommandName.CHE_TAO_TRANG_BI_THIEN_SU, "Chế tạo\ntrang bị\nThiên sứ", player, npc));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Đổi Đá cấp 9", player, UpgradeType.DOI_DA_9));
                }
                break;
            }

            case NpcName.BEERUS: {
                if (npc != null) {
                    commands.add(new Command(CommandName.REMOVE_OPTION, "Xóa chỉ số cải trang", player, npc));
                    commands.add(new Command(CommandName.REMOVE_STAR, "Gỡ Sao Pha Lê", player, npc));
                    commands.add(new Command(CommandName.OPEN_UPGRADE, "Đổi Capsule Huyền Bí", player, UpgradeType.DOI_CAPSULE_HUYEN_BI));
                }
                break;
            }

            case NpcName.MA_BAO_VE: {
                commands.add(new Command(CommandName.MENU_MA_BAO_VE, "Mã bảo vệ", player));
                break;
            }

        }
        if (commands.isEmpty()) {
            commands.add(new Command(CommandName.CANCEL, "Đóng", player));
            player.createMenu(npcId, (npc != null ? (npc.template.name + ": ") : " ") + "Cậu tìm ta có việc gì?", commands);
            return;
        }
        player.createMenu(npcId, (npc != null ? (npc.template.name + ": ") : " ") + content.toString(), commands);
    }

    public void confirmMenu(int npcId, int select) {
        if (player.commands == null || player.commands.isEmpty() || select < 0 || select >= player.commands.size()) {
            return;
        }
        Command command = player.commands.get(select);
        if (command != null) {
            command.performAction();
        }
    }
}
