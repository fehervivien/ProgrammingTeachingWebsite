# ProgrammingTeachingWebsite

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