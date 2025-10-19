// ============================ MOCK USERS ============================
const MOCK_USERS = [
  {
    email: "6509650187@dome.tu.ac.th",
    password: "1234",
    name: "อิทธิเชษฐ์ หงษ์วรพัฒน์",
    student_id: "6509650187"
  },
  {
    email: "student@dome.tu.ac.th",
    password: "password",
    name: "Student Demo",
    student_id: "6000000000"
  },
  {
    email: "test@dome.tu.ac.th",
    password: "1111",
    name: "ทดสอบ ระบบ",
    student_id: "6700000001"
  }
];

// ============================ AUTH FUNCTIONS ============================

// ✅ เก็บ session ลง localStorage
function setAuthSession(user) {
  const session = {
    name: user.name,
    student_id: user.student_id,
    email: user.email,
    login_time: Date.now()
  };
  localStorage.setItem("auth", JSON.stringify(session));
}

// ✅ ดึง session จาก localStorage
function getAuthSession() {
  try {
    const data = localStorage.getItem("auth");
    return data ? JSON.parse(data) : null;
  } catch {
    return null;
  }
}

// ✅ ลบ session
function clearAuthSession() {
  localStorage.removeItem("auth");
}

// ✅ login (mock)
async function login(email, password) {
  const found = MOCK_USERS.find(
    u => u.email === email && u.password === password
  );
  if (!found) {
    return { ok: false, message: "อีเมลหรือรหัสผ่านไม่ถูกต้อง" };
  }
  setAuthSession(found);
  return { ok: true, user: found };
}

// ✅ ตรวจว่าล็อกอินหรือยัง (ใช้ในทุกหน้า)
function requireAuth(redirectTo = "login.html") {
  const s = getAuthSession();
  if (!s) {
    window.location.href = redirectTo;
    throw new Error("Unauthorized");
  }
  return s;
}

// ✅ logout
function logout(redirectTo = "login.html") {
  clearAuthSession();
  window.location.href = redirectTo;
}

// ✅ แสดงชื่อ + รหัส นศ. ใน header
function fillUserHeader(nameSel = ".account-info .name", idSel = ".account-info .id") {
  const s = getAuthSession();
  if (!s) return;
  const nameEl = document.querySelector(nameSel);
  const idEl = document.querySelector(idSel);
  if (nameEl) nameEl.textContent = s.name;
  if (idEl) idEl.textContent = s.student_id;
}
