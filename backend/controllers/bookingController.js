const Booking = require('../models/Booking');
const Room = require('../models/Room');

const createBooking = async (req, res) => {
  const { roomId, guestName } = req.body;

  try {
    const room = await Room.findById(roomId);
    if (!room || room.currentStatus !== 'Available') {
      return res.status(400).json({ message: "Room is not available for booking" });
    }

    // 1. Create the Booking
    const booking = await Booking.create({
      guestName,
      room: roomId
    });

    // 2. 7-Star Automation: Update Room to 'Occupied'
    room.currentStatus = 'Occupied';
    await room.save();

    res.status(201).json({ message: "Booking Successful!", booking });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getAvailableRooms = async (req, res) => {
  try {
    const rooms = await Room.find({ currentStatus: 'Available' });
    res.json(rooms);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// @desc    Check out a guest and trigger cleaning
const checkout = async (req, res) => {
  const { bookingId } = req.params;

  try {
    const booking = await Booking.findById(bookingId).populate('room');
    if (!booking || booking.status === 'Completed') {
      return res.status(400).json({ message: "Invalid or already completed booking." });
    }

    // 1. Finalize the Booking
    booking.status = 'Completed';
    booking.checkOut = Date.now();
    await booking.save();

    // 2. 7-Star Automation: Room now needs cleaning!
    if (booking.room) {
      booking.room.currentStatus = 'Needs Cleaning';
      await booking.room.save();
    }

    res.json({ message: "Checkout successful. Room status: Needs Cleaning." });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Update your exports
module.exports = { createBooking, getAvailableRooms, checkout };