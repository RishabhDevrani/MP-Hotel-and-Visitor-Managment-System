const express = require('express');
const dotenv = require('dotenv');
const cors = require('cors');
const connectDB = require('./config/db');

// 1. Load env vars
dotenv.config();

// 2. Connect to database
connectDB();

// 3. Initialize Express
const app = express();

// 4. Middleware
app.use(cors());
app.use(express.json());

// 5. Route Imports (Keep them all together here for clarity)
const authRoutes = require('./routes/authRoutes');
const adminRoutes = require('./routes/adminRoutes');
const bookingRoutes = require('./routes/bookingRoutes');
const taskRoutes = require('./routes/taskRoutes');
const userRoutes = require('./routes/userRoutes'); // ✅ ADDED THIS LINE

// 6. Mount Routes
app.use('/api/auth', authRoutes);
app.use('/api/admin', adminRoutes);
app.use('/api/bookings', bookingRoutes);
app.use('/api/tasks', taskRoutes);
app.use('/api/users', userRoutes); // Now this will work!

// 7. Health check
app.get('/', (req, res) => {
  res.send('7-Star Hotel API is running...');
});

// 8. Start server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Server running in development mode on port ${PORT}`);
});