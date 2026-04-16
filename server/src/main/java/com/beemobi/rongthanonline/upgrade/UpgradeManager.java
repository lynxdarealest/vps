package com.beemobi.rongthanonline.upgrade;

import org.apache.log4j.Logger;

import java.util.HashMap;

public class UpgradeManager {
    private static final Logger logger = Logger.getLogger(UpgradeManager.class);
    private static UpgradeManager instance;
    public HashMap<UpgradeType, Upgrade> upgrades;

    public static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    public void init() {
        upgrades = new HashMap<>();
        upgrades.put(UpgradeType.UPGRADE_ITEM, new UpgradeItem(UpgradeType.UPGRADE_ITEM, "Cường hóa", "Cường hóa"));
        upgrades.put(UpgradeType.UPGRADE_STONE, new UpgradeStone(UpgradeType.UPGRADE_STONE, "Luyện đá", "Luyện đá"));
        upgrades.put(UpgradeType.CRYSTALLIZE, new Crystallize(UpgradeType.CRYSTALLIZE, "Làm phép", "Pha lê hóa"));
        upgrades.put(UpgradeType.POLISH, new Polish(UpgradeType.POLISH, "Làm phép", "Đánh bóng"));
        upgrades.put(UpgradeType.ENCHANT_ITEM, new EnchantItem(UpgradeType.ENCHANT_ITEM, "Làm phép", "Ép pha lê"));
        upgrades.put(UpgradeType.COMBINE, new Combine(UpgradeType.COMBINE, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.EQUIP_CRAFTING_3X, new EquipCrafting3x(UpgradeType.EQUIP_CRAFTING_3X, "Chế tạo", "Chế tạo"));
        upgrades.put(UpgradeType.EQUIP_CRAFTING_4X, new EquipCrafting4x(UpgradeType.EQUIP_CRAFTING_4X, "Chế tạo", "Chế tạo"));
        upgrades.put(UpgradeType.COSTUME_MERGING, new CostumeMerging(UpgradeType.COSTUME_MERGING, "Làm phép", "Ghép"));
        upgrades.put(UpgradeType.REFRESH_ITEM_3X, new RefreshItem3x(UpgradeType.REFRESH_ITEM_3X, "Tái chế", "Tái chế"));
        upgrades.put(UpgradeType.REFRESH_ITEM_4X, new RefreshItem4x(UpgradeType.REFRESH_ITEM_4X, "Tái chế", "Tái chế"));
        upgrades.put(UpgradeType.REFRESH_MAX_ITEM, new RefreshMaxItem(UpgradeType.REFRESH_MAX_ITEM, "Tái chế", "Tái chế"));
        upgrades.put(UpgradeType.EQUIP_CRAFTING_6X, new EquipCrafting6x(UpgradeType.EQUIP_CRAFTING_6X, "Chế tạo", "Chế tạo"));
        upgrades.put(UpgradeType.UP_STAR_PET, new UpStarPet(UpgradeType.UP_STAR_PET, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.EQUIP_CRAFTING_RIDER, new EquipCraftingRider(UpgradeType.EQUIP_CRAFTING_RIDER, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.CHANGE_OPTION_RIDER, new ChangeOptionRider(UpgradeType.CHANGE_OPTION_RIDER, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.UPGRADE_ITEM_RIDER, new UpgradeItemRider(UpgradeType.UPGRADE_ITEM_RIDER, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.PACK_AVATAR, new PackAvatar(UpgradeType.PACK_AVATAR, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.CRAFTING_PORATA, new CraftingPorata(UpgradeType.CRAFTING_PORATA, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.OPEN_OPTION_PORATA, new OpenOptionPorata(UpgradeType.OPEN_OPTION_PORATA, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.REMOVE_OPTION, new RemoveOption(UpgradeType.REMOVE_OPTION, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.CHE_TAO_THIEN_SU, new EquipCraftingThienSu(UpgradeType.CHE_TAO_THIEN_SU, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.PACK_THU_CUOI, new PackThuCuoi(UpgradeType.PACK_THU_CUOI, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.UPGRADE_PORATA, new UpgradePorata(UpgradeType.UPGRADE_PORATA, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.REMOVE_STAR, new RemoveStar(UpgradeType.REMOVE_STAR, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.GHEP_HUY_HIEU, new GhepHuyHieu(UpgradeType.GHEP_HUY_HIEU, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.TACH_HUY_HIEU, new TachHuyHieu(UpgradeType.TACH_HUY_HIEU, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.GHEP_CAI_TRANG, new GhepCaiTrang(UpgradeType.GHEP_CAI_TRANG, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.TACH_CAI_TRANG, new TachCaiTrang(UpgradeType.TACH_CAI_TRANG, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.GHEP_DEO_LUNG, new GhepDeoLung(UpgradeType.GHEP_DEO_LUNG, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.TACH_DEO_LUNG, new TachDeoLung(UpgradeType.TACH_DEO_LUNG, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.GHEP_THU_CUOI, new GhepThuCuoi(UpgradeType.GHEP_THU_CUOI, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.TACH_THU_CUOI, new TachThuCuoi(UpgradeType.TACH_THU_CUOI, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.GHEP_AURA, new GhepAura(UpgradeType.GHEP_AURA, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.TACH_AURA, new TachAura(UpgradeType.TACH_AURA, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.GHEP_HUY_HIEU_DAI_GIA, new GhepHuyHieuDaiGia(UpgradeType.GHEP_HUY_HIEU_DAI_GIA, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.DOI_DA_9, new DoiDa9(UpgradeType.DOI_DA_9, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.DOI_CAPSULE_HUYEN_BI, new DoiCapsuleHuyenBi(UpgradeType.DOI_CAPSULE_HUYEN_BI, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.NANG_TRANG_BI_PET, new NangTrangBiPet(UpgradeType.NANG_TRANG_BI_PET, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.DOI_TRANG_BI_PET, new DoiTrangBiPet(UpgradeType.DOI_TRANG_BI_PET, "Làm phép", "Làm phép"));
        upgrades.put(UpgradeType.DOI_HOA_HONG, new DoiHoaHong(UpgradeType.DOI_HOA_HONG, "Làm phép", "Làm phép"));
    }
}
