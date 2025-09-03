import { Router } from "express";
import { PrismaClient } from "@prisma/client";
import { sign, hash, check } from "../auth";
import { requireAuth, requireAdmin } from "../middleware/auth";
const prisma = new PrismaClient();
export const router = Router();

// Admin invites/registers users
router.post("/register", requireAuth, requireAdmin, async (req, res) => {
  const { email, password, role } = req.body;
  const exists = await prisma.user.findUnique({ where: { email } });
  if (exists) return res.status(400).json({ error: "Email exists" });
  const hashed = await hash(password);
  const user = await prisma.user.create({ data: { email, password: hashed, role: role || "USER" } });
  res.json({ id: user.id, email: user.email, role: user.role });
});

// Login
router.post("/login", async (req, res) => {
  const { email, password } = req.body;
  const user = await prisma.user.findUnique({ where: { email } });
  if (!user) return res.status(401).json({ error: "Invalid creds" });
  const ok = await check(password, user.password);
  if (!ok) return res.status(401).json({ error: "Invalid creds" });
  const token = sign({ sub: user.id, role: user.role });
  res.json({ token });
});
