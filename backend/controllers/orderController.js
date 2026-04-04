const Order = require('../models/Order');

// Guest places an order
const placeOrder = async (req, res) => {
  try {
    const { items, totalAmount, roomNumber } = req.body;
    const order = await Order.create({
      guest: req.user._id,
      roomNumber,
      items,
      totalAmount
    });
    res.status(201).json(order);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Chef gets all pending orders
const getOrders = async (req, res) => {
  try {
    const orders = await Order.find().populate('guest', 'name').sort('-createdAt');
    res.json(orders);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = { placeOrder, getOrders };