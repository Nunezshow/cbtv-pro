import express from "express";
import cors from "cors";
import { json } from "body-parser";
import { router as users } from "./routes/users";
import { router as config } from "./routes/config";
import { router as playlists } from "./routes/playlists";
import { router as flags } from "./routes/flags";

const app = express();
app.use(cors());
app.use(json());

app.use("/users", users);
app.use("/config", config);
app.use("/playlists", playlists);
app.use("/flags", flags);

const port = process.env.PORT || 8080;
app.listen(port, () => console.log(`API running on :${port}`));
