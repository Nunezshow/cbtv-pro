import { Router } from "express";
import { PrismaClient } from "@prisma/client";
import { requireAuth, requireAdmin } from "../middleware/auth";
const prisma = new PrismaClient();
export const router = Router();

router.get("/", requireAuth, async (_req, res) => {
  const list = await prisma.playlist.findMany({ where: { isGlobal: true }});
  res.json(list);
});

router.post("/", requireAuth, requireAdmin, async (req, res) => {
  const { name, url, isGlobal } = req.body;
  const created = await prisma.playlist.create({ data: { name, url, isGlobal: isGlobal ?? true }});
  res.json(created);
});
