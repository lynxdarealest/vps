const TOKEN_KEY = "rto_cpanel_token";

const els = {
  loginPanel: document.getElementById("loginPanel"),
  appPanel: document.getElementById("appPanel"),
  usernameInput: document.getElementById("usernameInput"),
  passwordInput: document.getElementById("passwordInput"),
  loginBtn: document.getElementById("loginBtn"),
  logoutBtn: document.getElementById("logoutBtn"),
  refreshAllBtn: document.getElementById("refreshAllBtn"),
  welcomeText: document.getElementById("welcomeText"),
  dashboardCards: document.getElementById("dashboardCards"),
  eventStatus: document.getElementById("eventStatus"),
  eventStatusBtn: document.getElementById("eventStatusBtn"),
  eventKeySelect: document.getElementById("eventKeySelect"),
  eventStartInput: document.getElementById("eventStartInput"),
  eventEndInput: document.getElementById("eventEndInput"),
  activateEventBtn: document.getElementById("activateEventBtn"),
  deactivateEventBtn: document.getElementById("deactivateEventBtn"),
  reloadLogsBtn: document.getElementById("reloadLogsBtn"),
  logsTableBody: document.getElementById("logsTableBody"),
  userKeywordInput: document.getElementById("userKeywordInput"),
  selectedUserIdInput: document.getElementById("selectedUserIdInput"),
  searchUserBtn: document.getElementById("searchUserBtn"),
  usersTableBody: document.getElementById("usersTableBody"),
  newPasswordInput: document.getElementById("newPasswordInput"),
  changePasswordBtn: document.getElementById("changePasswordBtn"),
  grantUserIdInput: document.getElementById("grantUserIdInput"),
  grantCurrencySelect: document.getElementById("grantCurrencySelect"),
  grantAmountInput: document.getElementById("grantAmountInput"),
  grantReasonInput: document.getElementById("grantReasonInput"),
  grantBtn: document.getElementById("grantBtn"),
  giftCodeInput: document.getElementById("giftCodeInput"),
  giftExpiryInput: document.getElementById("giftExpiryInput"),
  giftLevelInput: document.getElementById("giftLevelInput"),
  giftTaskInput: document.getElementById("giftTaskInput"),
  giftActiveInput: document.getElementById("giftActiveInput"),
  giftItemsInput: document.getElementById("giftItemsInput"),
  createGiftBtn: document.getElementById("createGiftBtn"),
  reloadGiftBtn: document.getElementById("reloadGiftBtn"),
  giftList: document.getElementById("giftList"),
  toast: document.getElementById("toast"),
};

const state = {
  token: localStorage.getItem(TOKEN_KEY) || "",
  me: null,
};

function showToast(text, type = "ok") {
  els.toast.textContent = text;
  els.toast.classList.remove("hidden", "ok", "error");
  els.toast.classList.add(type === "error" ? "error" : "ok");
  window.clearTimeout(showToast.timer);
  showToast.timer = window.setTimeout(() => {
    els.toast.classList.add("hidden");
  }, 3200);
}

async function apiRequest(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };

  if (state.token) {
    headers["X-CPanel-Token"] = state.token;
  }

  const response = await fetch(`/api/cpanel${path}`, {
    method: options.method || "GET",
    headers,
    body: options.body ? JSON.stringify(options.body) : undefined,
  });

  if (!response.ok) {
    let message = `HTTP ${response.status}`;
    try {
      const body = await response.json();
      message = body.message || body.error || message;
    } catch (e) {
      const text = await response.text();
      if (text) {
        message = text;
      }
    }
    throw new Error(message);
  }

  return response.json();
}

function formatTime(ms) {
  if (!ms) {
    return "-";
  }
  return new Date(ms).toLocaleString("vi-VN");
}

function toLocalInputDate(ms) {
  const d = new Date(ms);
  const pad = (n) => String(n).padStart(2, "0");
  const yyyy = d.getFullYear();
  const mm = pad(d.getMonth() + 1);
  const dd = pad(d.getDate());
  const hh = pad(d.getHours());
  const mi = pad(d.getMinutes());
  return `${yyyy}-${mm}-${dd}T${hh}:${mi}`;
}

function fromLocalInputDate(value) {
  if (!value) {
    return null;
  }
  const ms = new Date(value).getTime();
  return Number.isNaN(ms) ? null : ms;
}

function setSelectedUser(userId) {
  els.selectedUserIdInput.value = userId;
  els.grantUserIdInput.value = userId;
}

function renderCards(data) {
  const items = [
    { label: "Online Players", value: data.onlinePlayers ?? 0 },
    { label: "Server", value: `#${data.serverId || 1}` },
    { label: "Version", value: data.serverVersion || "?" },
    { label: "Pending Orders", value: data.pendingRechargeOrders ?? 0 },
  ];

  els.dashboardCards.innerHTML = items
    .map((item) => `
      <div class="metric">
        <p>${item.label}</p>
        <h4>${item.value}</h4>
      </div>
    `)
    .join("");
}

function renderEventStatus(eventData) {
  if (!eventData || !eventData.active) {
    els.eventStatus.innerHTML = "<strong>Khong co event dang chay.</strong>";
    return;
  }

  els.eventStatus.innerHTML = `
    <strong>${eventData.name || eventData.className}</strong><br>
    Key: ${eventData.key || "custom"}<br>
    Bat dau: ${formatTime(eventData.startTime)}<br>
    Ket thuc: ${formatTime(eventData.endTime)}
  `;
}

function renderLogs(logs) {
  els.logsTableBody.innerHTML = logs
    .map((row) => `
      <tr>
        <td>${row.id ?? ""}</td>
        <td>${row.username || "unknown"} (#${row.userId || "?"})</td>
        <td>${row.typeName || "UNKNOWN"}</td>
        <td>${row.diamond || 0}</td>
        <td>${row.coin || 0}</td>
        <td>${row.statusName || "PENDING"}</td>
        <td>${row.orderCode || "-"}</td>
        <td>${formatTime(row.createTime)}</td>
      </tr>
    `)
    .join("");
}

function renderUsers(users) {
  els.usersTableBody.innerHTML = users
    .map((user) => {
      const chars = Array.isArray(user.characters) ? user.characters : [];
      const charNames = chars.length
        ? chars.map((c) => `${c.name || "?"}${c.online ? " (online)" : ""}`).join(", ")
        : "Khong co nhan vat";
      const resources = chars.length
        ? chars
            .map((c) => `Ngoc:${c.diamond || 0} Ruby:${c.ruby || 0} Vang:${c.gold || 0}`)
            .join(" | ")
        : "-";

      return `
        <tr>
          <td>${user.username || "unknown"}<br><small>ID: ${user.id}</small></td>
          <td>${charNames}</td>
          <td>${resources}</td>
          <td><button class="btn btn-ghost" data-pick-user="${user.id}">Chon</button></td>
        </tr>
      `;
    })
    .join("");

  els.usersTableBody.querySelectorAll("button[data-pick-user]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number(btn.getAttribute("data-pick-user"));
      setSelectedUser(id);
      showToast(`Da chon user #${id}`);
    });
  });
}

function renderGiftList(giftCodes) {
  if (!giftCodes.length) {
    els.giftList.innerHTML = "Chua co gift code.";
    return;
  }

  els.giftList.innerHTML = giftCodes
    .slice(0, 12)
    .map((g) => `#${g.id} - ${g.code} | het han: ${formatTime(g.expiryTime)}`)
    .join("<br>");
}

async function loadDashboard() {
  const data = await apiRequest("/dashboard");
  renderCards(data);
  renderEventStatus(data.activeEvent);
}

async function loadEventCatalog() {
  const events = await apiRequest("/events/catalog");
  els.eventKeySelect.innerHTML = events
    .map((e) => `<option value="${e.key}">${e.label} (${e.className})</option>`)
    .join("");
}

async function loadEventStatus() {
  const data = await apiRequest("/events/status");
  renderEventStatus(data);
}

async function loadLogs() {
  const logs = await apiRequest("/recharge-logs?limit=120");
  renderLogs(logs);
}

async function loadUsers() {
  const keyword = encodeURIComponent(els.userKeywordInput.value.trim());
  const users = await apiRequest(`/users?limit=60&keyword=${keyword}`);
  renderUsers(users);
}

async function loadGiftCodes() {
  const giftCodes = await apiRequest("/gift-codes?limit=40");
  renderGiftList(giftCodes);
}

async function refreshAll() {
  await Promise.all([loadDashboard(), loadLogs(), loadUsers(), loadGiftCodes(), loadEventStatus()]);
}

async function login() {
  const username = els.usernameInput.value.trim();
  const password = els.passwordInput.value.trim();

  if (!username || !password) {
    showToast("Nhap tai khoan va mat khau", "error");
    return;
  }

  try {
    const data = await apiRequest("/auth/login", {
      method: "POST",
      body: { username, password },
      headers: {},
    });

    state.token = data.token;
    localStorage.setItem(TOKEN_KEY, state.token);

    await initApp();
    showToast("Dang nhap thanh cong");
  } catch (error) {
    showToast(error.message, "error");
  }
}

async function logout() {
  try {
    await apiRequest("/auth/logout", { method: "POST" });
  } catch (error) {
    console.warn(error);
  }

  state.token = "";
  localStorage.removeItem(TOKEN_KEY);
  els.appPanel.classList.add("hidden");
  els.loginPanel.classList.remove("hidden");
}

async function initApp() {
  const me = await apiRequest("/auth/me");
  state.me = me;

  els.welcomeText.textContent = `Xin chao ${me.username}`;
  els.loginPanel.classList.add("hidden");
  els.appPanel.classList.remove("hidden");

  setDefaultDates();
  await loadEventCatalog();
  await refreshAll();
}

function setDefaultDates() {
  const now = Date.now();
  const after7d = now + 7 * 24 * 60 * 60 * 1000;

  if (!els.eventStartInput.value) {
    els.eventStartInput.value = toLocalInputDate(now);
  }
  if (!els.eventEndInput.value) {
    els.eventEndInput.value = toLocalInputDate(after7d);
  }
  if (!els.giftExpiryInput.value) {
    els.giftExpiryInput.value = toLocalInputDate(after7d);
  }
}

async function activateEvent() {
  const key = els.eventKeySelect.value;
  const startAt = fromLocalInputDate(els.eventStartInput.value);
  const endAt = fromLocalInputDate(els.eventEndInput.value);

  try {
    const data = await apiRequest("/events/activate", {
      method: "POST",
      body: { key, startAt, endAt },
    });
    renderEventStatus(data);
    await loadDashboard();
    showToast("Da kich hoat event");
  } catch (error) {
    showToast(error.message, "error");
  }
}

async function deactivateEvent() {
  try {
    await apiRequest("/events/deactivate", { method: "POST" });
    await loadEventStatus();
    await loadDashboard();
    showToast("Da tat event");
  } catch (error) {
    showToast(error.message, "error");
  }
}

async function changePassword() {
  const userId = Number(els.selectedUserIdInput.value);
  const newPassword = els.newPasswordInput.value.trim();

  if (!userId || !newPassword) {
    showToast("Can chon User ID va nhap mat khau moi", "error");
    return;
  }

  try {
    const data = await apiRequest(`/users/${userId}/password`, {
      method: "POST",
      body: { newPassword },
    });
    showToast(data.message || "Da doi mat khau");
  } catch (error) {
    showToast(error.message, "error");
  }
}

async function grantCurrency() {
  const userId = Number(els.grantUserIdInput.value);
  const currency = els.grantCurrencySelect.value;
  const amount = Number(els.grantAmountInput.value);
  const reason = els.grantReasonInput.value.trim();

  if (!userId || !amount) {
    showToast("Can nhap User ID va so luong", "error");
    return;
  }

  try {
    const data = await apiRequest("/grants", {
      method: "POST",
      body: { userId, currency, amount, reason },
    });
    showToast(data.message || "Cap tai nguyen thanh cong");
    await Promise.all([loadUsers(), loadLogs(), loadDashboard()]);
  } catch (error) {
    showToast(error.message, "error");
  }
}

async function createGiftCode() {
  const code = els.giftCodeInput.value.trim();
  const expiryAt = fromLocalInputDate(els.giftExpiryInput.value);
  const levelRequire = Number(els.giftLevelInput.value || 1);
  const taskRequire = Number(els.giftTaskInput.value || 0);
  const activePointRequire = Number(els.giftActiveInput.value || 0);
  const itemsJson = els.giftItemsInput.value.trim();

  if (!code || !itemsJson) {
    showToast("Code va itemsJson khong duoc rong", "error");
    return;
  }

  try {
    const data = await apiRequest("/gift-codes", {
      method: "POST",
      body: {
        code,
        expiryAt,
        levelRequire,
        taskRequire,
        activePointRequire,
        itemsJson,
      },
    });
    showToast(data.message || "Tao gift code thanh cong");
    await loadGiftCodes();
  } catch (error) {
    showToast(error.message, "error");
  }
}

function bindEvents() {
  els.loginBtn.addEventListener("click", login);
  els.passwordInput.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      login();
    }
  });

  els.logoutBtn.addEventListener("click", logout);
  els.refreshAllBtn.addEventListener("click", () => refreshAll().catch((e) => showToast(e.message, "error")));

  els.eventStatusBtn.addEventListener("click", () => loadEventStatus().catch((e) => showToast(e.message, "error")));
  els.activateEventBtn.addEventListener("click", activateEvent);
  els.deactivateEventBtn.addEventListener("click", deactivateEvent);

  els.reloadLogsBtn.addEventListener("click", () => loadLogs().catch((e) => showToast(e.message, "error")));
  els.searchUserBtn.addEventListener("click", () => loadUsers().catch((e) => showToast(e.message, "error")));
  els.changePasswordBtn.addEventListener("click", changePassword);
  els.grantBtn.addEventListener("click", grantCurrency);

  els.reloadGiftBtn.addEventListener("click", () => loadGiftCodes().catch((e) => showToast(e.message, "error")));
  els.createGiftBtn.addEventListener("click", createGiftCode);
}

async function bootstrap() {
  bindEvents();

  if (!state.token) {
    return;
  }

  try {
    await initApp();
  } catch (error) {
    state.token = "";
    localStorage.removeItem(TOKEN_KEY);
    showToast("Phien dang nhap het han, vui long dang nhap lai", "error");
  }
}

bootstrap();
