const mongoose = require('mongoose');
const cities = require('./cities')
const { places, descriptors } = require('./seedHelpers')
const business = require('../models/schema');
mongoose.connect('mongodb://localhost:27017/yelp-camp', {
    useNewUrlParser: true,
    useCreateIndex: true,
    useUnifiedTopology: true
});

const db = mongoose.connection;
db.on("error", console.error.bind(console, 'connection error:'));
db.once("open", () => {
    console.log("Database connected")
});


const sample = array => array[Math.floor(Math.random() * array.length)];


const seedDB = async () => {
    await business.deleteMany({});
    for (let i = 0; i < 10; i++) {
        const random1000 = Math.floor(Math.random() * 1000);
        const businesses = new business({
            author: '60474d753f7bf90694062f77',
            location: `${cities[random1000].city}, ${cities[random1000].state}`,
            title: `${sample(descriptors)} ${sample(places)}`,
            geometry: {
                type: "Point",
                coordinates: [
                    cities[random1000].longitude,
                    cities[random1000].latitude
                ]
            },
            images: [
                {
                    url: 'https://res.cloudinary.com/duuiorcnz/image/upload/v1615521292/BizzReview/n4mzgtjemyax4geydme1.jpg',
                    filename: 'BizzReview/n4mzgtjemyax4geydme1'
                },
                {
                    url: 'https://res.cloudinary.com/duuiorcnz/image/upload/v1615521291/BizzReview/kwl7ksk45j1gytmpddvy.jpg',
                    filename: 'BizzReview/kwl7ksk45j1gytmpddvy'
                }
            ],
            description: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Quos laboriosam ex ipsam possimus molestias. Ab quam iusto dignissimos quas, id similique expedita dolorum, ex veritatis, iste nostrum suscipit deserunt ipsa?'
        })
        await businesses.save();
    }
}

seedDB().then(() => {
    mongoose.connection.close()
})