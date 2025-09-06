import { Router } from "express";
import { PrismaClient } from "@prisma/client";
import { requireAuth } from "../middleware/auth";
const prisma = new PrismaClient();
export const router = Router();

// App pulls this on startup: playlists + feature flags
router.get("/", requireAuth, async (req, res) => {
  const userId = (req as any).user.sub as string;
  const flags = await prisma.featureFlags.findUnique({ where: { userId } });
  const playlists = await prisma.playlist.findMany({ where: { isGlobal: true }});
  res.json({ playlists, featureFlags: flags?.flagsJson ?? {} });
});
