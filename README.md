# ProgrammingTeachingWebsite

## Database setup

With docker run this command to setup the PostgreSQL DB used by the application:

```bash
docker run -d --name progteaching-postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_DB=progteaching_db -e POSTGRES_HOST_AUTH_METHOD=trust postgres:18.6`
```

## Frontend / Tailwind CSS

The project uses Tailwind CSS through npm and PostCSS. The source stylesheet is
`src/main/frontend/css/input.css`, while the generated stylesheet is written to
`src/main/resources/static/css/tailwind.css`.

Install the frontend dependencies and build the stylesheet manually with:

```bash
npm install
npm run build:css
```

During development, use `npm run watch:css` to rebuild the stylesheet when
templates or CSS sources change. A regular Maven build also installs the pinned
Node/npm versions, runs `npm ci`, and builds Tailwind automatically:

```bash
./mvnw clean package
```

Include the generated stylesheet in a Thymeleaf template with:

```html
<link rel="stylesheet" th:href="@{/css/tailwind.css}">
```
