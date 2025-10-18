async function login(email, password) {
  try {
    const res = await fetch("http://localhost:8081/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    if (!res.ok) return { ok: false, message: "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง" };

    const data = await res.json();
    if (data.ok) {
      // เก็บข้อมูลผู้ใช้ไว้ใน localStorage
      localStorage.setItem("auth", JSON.stringify({
        name: data.name,
        studentId: data.studentId,
        email: email
      }));
      return { ok: true, message: "เข้าสู่ระบบสำเร็จ" };
    } else {
      return { ok: false, message: data.message };
    }

  } catch (err) {
    console.error("Login error:", err);
    return { ok: false, message: "เชื่อมต่อระบบไม่สำเร็จ" };
  }
}

function getAuthSession() {
  return JSON.parse(localStorage.getItem("auth") || "null");
}

function logout() {
  localStorage.removeItem("auth");
  window.location.href = "login.html";
}

function requireAuth(redirectTo = "login.html") {
    //const session = getAuthSession();
    //if (!session) {
    //    window.location.href = redirectTo;
    //    throw new Error("Unauthorized");
    //}
    //return session;
	return { name: "Test User", id: "6509999999" }; // mock user สำหรับทดสอบ
}

function fillUserHeader() {
  const user = getAuthSession();
  if (user) {
    document.querySelector(".account-info .name").textContent = user.name;
    document.querySelector(".account-info .id").textContent = user.studentId;
  }
}
