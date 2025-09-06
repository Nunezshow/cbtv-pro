diff --git a/backend/src/routes/users.ts b/backend/src/routes/users.ts
index dcd7422c190dc52ef0c24bc213d40cadb25a35b1..870aaaef7683a114adfcad3ba3eeddeec4b0b354 100644
--- a/backend/src/routes/users.ts
+++ b/backend/src/routes/users.ts
@@ -1,27 +1,55 @@
 import { Router } from "express";
 import { PrismaClient } from "@prisma/client";
+import rateLimit from "express-rate-limit";
 import { sign, hash, check } from "../auth";
 import { requireAuth, requireAdmin } from "../middleware/auth";
+
 const prisma = new PrismaClient();
 export const router = Router();
 
+const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
+const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;
+
+const loginLimiter = rateLimit({
+  windowMs: 60 * 1000,
+  max: 5,
+  standardHeaders: true,
+  legacyHeaders: false,
+  message: { error: "Too many login attempts, please try again later" },
+});
+
 // Admin invites/registers users
 router.post("/register", requireAuth, requireAdmin, async (req, res) => {
   const { email, password, role } = req.body;
+  if (!email) return res.status(400).json({ error: "Email is required" });
+  if (!emailRegex.test(email))
+    return res.status(400).json({ error: "Invalid email format" });
+  if (!password) return res.status(400).json({ error: "Password is required" });
+  if (!passwordRegex.test(password))
+    return res.status(400).json({
+      error:
+        "Password must be at least 8 characters and include upper and lower case letters, a number, and a symbol",
+    });
   const exists = await prisma.user.findUnique({ where: { email } });
   if (exists) return res.status(400).json({ error: "Email exists" });
   const hashed = await hash(password);
-  const user = await prisma.user.create({ data: { email, password: hashed, role: role || "USER" } });
+  const user = await prisma.user.create({
+    data: { email, password: hashed, role: role || "USER" },
+  });
   res.json({ id: user.id, email: user.email, role: user.role });
 });
 
 // Login
-router.post("/login", async (req, res) => {
+router.post("/login", loginLimiter, async (req, res) => {
   const { email, password } = req.body;
+  if (!email) return res.status(400).json({ error: "Email is required" });
+  if (!password) return res.status(400).json({ error: "Password is required" });
+  if (!emailRegex.test(email))
+    return res.status(400).json({ error: "Invalid email format" });
   const user = await prisma.user.findUnique({ where: { email } });
   if (!user) return res.status(401).json({ error: "Invalid creds" });
   const ok = await check(password, user.password);
   if (!ok) return res.status(401).json({ error: "Invalid creds" });
   const token = sign({ sub: user.id, role: user.role });
   res.json({ token });
 });
