const Business = require('../models/schema');
const mbxGeocoding = require('@mapbox/mapbox-sdk/services/geocoding')
const mapBoxToken = process.env.MAPBOX_TOKEN;
const geocoder = mbxGeocoding({ accessToken: mapBoxToken });
const { cloudinary } = require('../cloudinary');

module.exports.index = async (req, res) => {
    const businesses = await Business.find({});
    res.render('businesses/index', { businesses })
}

module.exports.renderNewForm = (req, res) => {
    res.render('businesses/new')
}

module.exports.createbusiness = async (req, res, next) => {
    const geoData = await geocoder.forwardGeocode({
        query: req.body.business.location,
        limit: 1
    }).send()

    const business = new Business(req.body.business);
    business.geometry = geoData.body.features[0].geometry;
    business.images = req.files.map(f => ({ url: f.path, filename: f.filename }))
    business.author = req.user._id
    await business.save();
    req.flash('success', 'Succesfully made a new business!')
    res.redirect(`/businesses/${business._id}`)
}

module.exports.showbusiness = async (req, res) => {
    const business = await Business.findById(req.params.id).populate({
        path: 'reviews',
        populate: {
            path: 'author'
        }
    }).populate('author')
    if (!business) {
        req.flash('error', 'Cannot find that business!')
        res.redirect('/businesses')
    }
    res.render('businesses/show', { business })
}

module.exports.renderEditForm = async (req, res) => {
    const { id } = req.params;
    const business = await Business.findById(id)
    if (!business) {
        req.flash('error', 'Cannot edit that business - does not exist')
        return res.redirect('/businesses');
    }
    res.render('businesses/edit', { business })
}


module.exports.updatebusiness = async (req, res) => {

    const geoData = await geocoder.forwardGeocode({
        query: req.body.business.location,
        limit: 1
    }).send()

    const { id } = req.params;
    const business = await Business.findByIdAndUpdate(id, { ...req.body.business });
    const imgs = req.files.map(f => ({ url: f.path, filename: f.filename }));
    business.images.push(...imgs);
    business.geometry = geoData.body.features[0].geometry;

    await business.save();
    if (req.body.deleteImages) {
        for (let filename of req.body.deleteImages) {
            await cloudinary.uploader.destroy(filename);
        }
        await business.updateOne({ $pull: { images: { filename: { $in: req.body.deleteImages } } } })
    }
    req.flash('success', 'Successfully updated business!');
    res.redirect(`/businesses/${business._id}`)
}

module.exports.deletebusiness = async (req, res) => {
    const { id } = req.params;
    await Business.findByIdAndDelete(id);
    req.flash('success', 'Successfully deleted business!');
    res.redirect('/businesses')
}