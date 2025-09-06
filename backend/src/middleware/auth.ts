import { Request, Response, NextFunction } from "express";
import { verify } from "../auth";

export function requireAuth(req: Request, res: Response, next: NextFunction) {
  const header = req.headers.authorization || "";
  const token = header.replace("Bearer ", "");
  try {
    const decoded = verify(token);
    (req as any).user = decoded;
    next();
  } catch {
    res.status(401).json({ error: "Unauthorized" });
  }
}

export function requireAdmin(req: Request, res: Response, next: NextFunction) {
  if ((req as any).user?.role === "ADMIN") return next();
  return res.status(403).json({ error: "Forbidden" });
}
