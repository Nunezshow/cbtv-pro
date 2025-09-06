diff --git a//dev/null b/backend/src/routes/users.test.ts
index 0000000000000000000000000000000000000000..e1af4e248297fd725aa51faf2cf100cca6e9818b 100644
--- a//dev/null
+++ b/backend/src/routes/users.test.ts
@@ -0,0 +1,88 @@
+import request from 'supertest';
+import express from 'express';
+import { hash } from '../auth';
+
+jest.mock('../middleware/auth', () => ({
+  requireAuth: (_req: any, _res: any, next: any) => next(),
+  requireAdmin: (_req: any, _res: any, next: any) => next(),
+}));
+
+const mockPrisma = {
+  user: {
+    findUnique: jest.fn(),
+    create: jest.fn(),
+  },
+};
+
+jest.mock('@prisma/client', () => ({
+  PrismaClient: jest.fn(() => mockPrisma),
+}));
+
+import { router } from './users';
+
+let app: express.Express;
+beforeEach(() => {
+  app = express();
+  app.use(express.json());
+  app.use('/users', router);
+  mockPrisma.user.findUnique.mockReset();
+  mockPrisma.user.create.mockReset();
+});
+
+describe('POST /users/register', () => {
+  it('registers with valid input', async () => {
+    mockPrisma.user.findUnique.mockResolvedValue(null);
+    mockPrisma.user.create.mockResolvedValue({ id: 1, email: 'test@example.com', role: 'USER' });
+    const res = await request(app)
+      .post('/users/register')
+      .send({ email: 'test@example.com', password: 'Strong1!', role: 'USER' });
+    expect(res.status).toBe(200);
+    expect(res.body).toEqual({ id: 1, email: 'test@example.com', role: 'USER' });
+  });
+
+  it('rejects weak passwords', async () => {
+    const res = await request(app)
+      .post('/users/register')
+      .send({ email: 'test@example.com', password: 'weak', role: 'USER' });
+    expect(res.status).toBe(400);
+    expect(res.body.error).toMatch(/Password must/);
+  });
+});
+
+describe('POST /users/login', () => {
+  it('logs in with valid credentials', async () => {
+    const hashed = await hash('Strong1!');
+    mockPrisma.user.findUnique.mockResolvedValue({
+      id: 1,
+      email: 'test@example.com',
+      password: hashed,
+      role: 'USER',
+    });
+    const res = await request(app)
+      .post('/users/login')
+      .send({ email: 'test@example.com', password: 'Strong1!' });
+    expect(res.status).toBe(200);
+    expect(res.body.token).toBeDefined();
+  });
+
+  it('fails when password missing', async () => {
+    const res = await request(app)
+      .post('/users/login')
+      .send({ email: 'test@example.com' });
+    expect(res.status).toBe(400);
+    expect(res.body.error).toMatch(/Password is required/);
+  });
+
+  it('rate limits repeated attempts', async () => {
+    mockPrisma.user.findUnique.mockResolvedValue(null);
+    for (let i = 0; i < 5; i++) {
+      await request(app)
+        .post('/users/login')
+        .send({ email: 'test@example.com', password: 'Strong1!' });
+    }
+    const res = await request(app)
+      .post('/users/login')
+      .send({ email: 'test@example.com', password: 'Strong1!' });
+    expect(res.status).toBe(429);
+  });
+});
