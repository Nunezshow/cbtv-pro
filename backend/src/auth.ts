import jwt from "jsonwebtoken";
import bcrypt from "bcryptjs";

const secret = process.env.JWT_SECRET || "dev";

export const sign = (payload: object) =>
  jwt.sign(payload, secret, { expiresIn: "30d" });

export const verify = (token: string) =>
  jwt.verify(token, secret) as { sub: string; role: string };

export const hash = async (pwd: string) => bcrypt.hash(pwd, 10);
export const check = async (pwd: string, hashVal: string) => bcrypt.compare(pwd, hashVal);
