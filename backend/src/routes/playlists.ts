diff --git a/backend/src/routes/config.ts b/backend/src/routes/config.ts
index 67889e430a6d1f4820ea6e8d343d8ab99fef5726..1af4cd98602787cfce6f59cad50f554a9a268521 100644
--- a/backend/src/routes/config.ts
+++ b/backend/src/routes/config.ts
@@ -1,13 +1,17 @@
 import { Router } from "express";
 import { PrismaClient } from "@prisma/client";
 import { requireAuth } from "../middleware/auth";
 const prisma = new PrismaClient();
 export const router = Router();
 
 // App pulls this on startup: playlists + feature flags
 router.get("/", requireAuth, async (req, res) => {
   const userId = (req as any).user.sub as string;
   const flags = await prisma.featureFlags.findUnique({ where: { userId } });
-  const playlists = await prisma.playlist.findMany({ where: { isGlobal: true }});
+  const playlists = await prisma.playlist.findMany({
+    where: {
+      OR: [{ isGlobal: true }, { userId }],
+    },
+  });
   res.json({ playlists, featureFlags: flags?.flagsJson ?? {} });
 });
