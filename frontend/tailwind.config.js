/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'gold': '#D4AF37',
        'royal-blue': '#1A237E',
        'luxury-black': '#0A0A0A',
      }
    },
  },
  plugins: [],
}