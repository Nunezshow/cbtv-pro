import { Router } from "express";
import { PrismaClient } from "@prisma/client";
import { requireAuth, requireAdmin } from "../middleware/auth";
const prisma = new PrismaClient();
export const router = Router();

router.post("/set", requireAuth, requireAdmin, async (req, res) => {
  const { userId, flags } = req.body; // flags: JSON object
  const upsert = await prisma.featureFlags.upsert({
    where: { userId },
    update: { flagsJson: flags },
    create: { userId, flagsJson: flags }
  });
  res.json(upsert);
});
