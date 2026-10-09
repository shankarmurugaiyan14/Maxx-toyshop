# Maxx Toy Shop — Angular Frontend

A first frontend prototype for the Maxx Toy Shop e-commerce project.

## Included

- Attractive responsive toy-store UI
- 10 placeholder products
- Category filtering
- Add to cart
- Quantity increase/decrease
- Remove from cart
- Cart drawer
- Checkout page
- Delivery form
- Demo payment selection
- Demo order confirmation
- INR pricing
- Mobile responsive layout

## Run

```bash
npm install
npm start
```

Then open:

http://localhost:4200

## Replacing product names and images

The products are currently defined in:

`src/app/app.component.ts`

Each product has:

```ts
{
  id: 1,
  name: 'Rainbow Building Blocks',
  category: 'Educational',
  price: 799,
  emoji: '🧱'
}
```

Later, replace `emoji` with an `image` field, for example:

```ts
image: 'assets/products/toy-1.jpg'
```

and place the image under:

`src/assets/products/`

## Next integration

This frontend is intentionally using local in-memory product/cart data for now.

The next version can connect it to:

Angular
   ↓
Spring Boot REST API
   ↓
MySQL

The checkout can then create a real order in the database.
