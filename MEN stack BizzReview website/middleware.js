const { businesseschema, reviewSchema } = require('./schemas.js')
const ExpressError = require('./utils/expressError');
const Business = require('./models/schema');
const Review = require('./models/review');


module.exports.isLoggedIn = (req, res, next) => {
    if (!req.isAuthenticated()) {
        // console.log(req.path, req.orginalUrl)
        req.session.returnTo = req.originalUrl.split("/reviews")[0];;
        req.flash('error', 'You must be signed in first!');
        return res.redirect('/login');
    }
    next();
}


module.exports.validatebusiness = (req, res, next) => {
    const { error } = businesseschema.validate(req.body);
    if (error) {
        const msg = error.details.map(el => el.message).join(',')
        throw new ExpressError(msg, 400);
    }
    else {
        next();
    }
}

module.exports.isAuthor = async (req, res, next) => {
    const { id } = req.params;
    const business = await Business.findById(id);
    if (!business.author.equals(req.user._id)) {
        req.flash('error', 'You do not have permission for that!')
        return res.redirect(`/businesses/${id}`);
    }
    next();
}

module.exports.validateReview = (req, res, next) => {
    const { error } = reviewSchema.validate(req.body);
    if (error) {
        const msg = error.details.map(el => el.message).join(',')
        throw new ExpressError(msg, 400);
    }
    else {
        next();
    }
}

module.exports.isReviewAuthor = async (req, res, next) => {
    const { id, reviewId } = req.params;
    const review = await Review.findById(reviewId);
    if (!review.author._id.equals(req.user._id)) {
        req.flash('error', 'You do not have permission for that!')
        return res.redirect(`/businesses/${id}`);
    }
    next();
}