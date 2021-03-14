const express = require('express');
const router = express.Router();
const businesses = require('../controllers/businesses')
const catchAsync = require('../utils/catchAsync');
const { isLoggedIn, isAuthor, validatebusiness } = require('../middleware');
const multer = require('multer')
const { storage } = require('../cloudinary')
const upload = multer({ storage })

router.route('/')
    .get(catchAsync(businesses.index))
    .post(isLoggedIn,
        upload.array('image'),
        validatebusiness,
        catchAsync(businesses.createbusiness))


router.get('/new',
    isLoggedIn,
    businesses.renderNewForm)

router.route('/:id')
    .get(catchAsync(businesses.showbusiness))
    .put(isLoggedIn,
        isAuthor,
        upload.array('image'),
        validatebusiness,
        catchAsync(businesses.updatebusiness))
    .delete(isLoggedIn,
        isAuthor,
        catchAsync(businesses.deletebusiness))

router.get('/:id/edit',
    isLoggedIn,
    isAuthor,
    catchAsync(businesses.renderEditForm))

module.exports = router;