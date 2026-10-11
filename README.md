# SDA Tithe Management System (Rwanda)

Web Technology project - Faculty of Information Technology, 2026-2027.
Tithe management for SDA churches in Rwanda with three roles (Member, Church, Tithe Manager) and a
four-language interface (Kinyarwanda, Kiswahili, English, French).

## Stack
| Layer | Technology |
|---|---|
| Frontend | React 18, Vite, react-router, react-i18next, plain responsive CSS |
| Backend | Spring Boot 3.3 (Java 17), layered monolith + event-driven notifications |
| Relational DB | PostgreSQL (members, churches, payments, receipts, managers) |
| Document DB | MongoDB (receipt snapshots, notification logs, audit logs) |
| Messaging | RabbitMQ (topic exchange `tithe.events`, queues `receipt.email` and `receipt.sms`) |
| Security | OAuth2 Resource Server with JWT bearer tokens, BCrypt, RBAC (`@PreAuthorize`) |
| DevOps | Docker Compose, GitHub Actions CI |

## Run everything
```bash
docker compose up --build
```
- App: http://localhost:5173
- API: http://localhost:8080
- RabbitMQ console: http://localhost:15672 (guest / guest)

## Demo accounts (password `1234` for all - demo seed data, stored BCrypt-hashed)
| Role | Login | What it can do |
|---|---|---|
| Member | `0788000001` (phone) or `member1@example.rw` | Pay tithe (no cash), see receipt, own history |
| Church | `CH001` (ID) or `Kigali Central SDA Church` (name) | Members, add member, weekly/monthly/yearly report |
| Tithe Manager | `manager` | All-church reports, record cash payments |

Other seeded churches: `CH002`, `CH003`; members `0788000001` ... `0788000012`.

## Run without Docker (development)
```bash
# 1. start PostgreSQL (db/user/password: tithe), MongoDB and RabbitMQ locally (or: docker compose up postgres mongo rabbitmq)
# 2. backend
cd backend && mvn spring-boot:run
# 3. frontend (proxies /api to localhost:8080)
cd frontend && npm install && npm run dev
```
Backend tests: `cd backend && mvn test`.

## REST API
| Method | Path | Role |
|---|---|---|
| POST | `/api/auth/login` `{identifier, password}` | public |
| POST | `/api/payments` | MEMBER |
| GET | `/api/payments/me` | MEMBER |
| GET | `/api/receipts/{receiptNo}` | owner member / own church / manager |
| GET, POST | `/api/church/members` | CHURCH |
| GET | `/api/church/payments?period=WEEK\|MONTH\|YEAR&offset=0&page=0&size=20` | CHURCH |
| GET | `/api/manager/churches` | TITHE_MANAGER |
| GET | `/api/manager/members?q=` | TITHE_MANAGER |
| GET | `/api/manager/payments?period=&offset=&page=&size=&churchId=` | TITHE_MANAGER |
| POST | `/api/manager/payments/cash` `{memberId, amount}` | TITHE_MANAGER |

`offset` moves through history (0 = current period, -1 = previous, ...).

## Design notes
- **Church scope comes from the token**, never from the request, so a church can only read its own data.
- **Payment + receipt** are saved in one database transaction. The RabbitMQ event and the MongoDB receipt
  snapshot are produced only after the commit, and a broker or MongoDB outage never undoes a payment.
- **Payment gateway is simulated.** `PaymentService.payDigital` is the single place to plug in MTN MoMo,
  Airtel Money or card providers.
- **Email/SMS are simulated** by `NotificationListener` (log + `notification_logs` collection).
- Receipt messages are generated in the member's preferred language (`MessageTemplates`).
- Server error messages are in English; the UI texts are fully translated. Please have the Kinyarwanda and
  Kiswahili translations in `frontend/src/i18n.js` reviewed by a native speaker before final submission.
- `password 1234` is for demonstration only; a real deployment must force a password change on first login
  and set `JWT_SECRET` and `SEED_DEMO_DATA=false`.

## Git workflow
```bash
git checkout -b feature/<name>
git commit -m "feat: ..."
git push -u origin feature/<name>   # then open a pull request to main
```
