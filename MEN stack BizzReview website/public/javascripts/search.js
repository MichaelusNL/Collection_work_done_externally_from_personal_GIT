
const searchBar = document.querySelector('#search');
const businessesElement = document.querySelector('#businesses');
const searchInput = document.querySelector('#searchInput');
searchBar.addEventListener("keyup", e => {
    businessesElement.innerHTML ='';
    for (business of businesses.features) {
        if(business.title.toLowerCase().indexOf(e.target.value.toLowerCase())>=0 ||
        business.location.toLowerCase().indexOf(e.target.value.toLowerCase())>=0){
            let title = business.title;
            let url = business.images[0] ? business.images[0].url : "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/600px-No_image_available.svg.png"
            let description = business.description;
            let location = business.location;
            let id = business._id;
            businessesElement.insertAdjacentHTML('beforeend', (setHtml(title, url, description, location, id)));

        }}        
})




const setHtml = (title, url, description, location, id) => {
    return `<div id ="businesses">
            <div class="card mb-3">
                <div class="row">
                    <div class="col-md-4">
                            <img class="img-fluid" alt="" src=${url}>
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title">
                                ${title}
                            </h5>
                            <p class="card-text">
                                ${description}
                            </p>

                            <p class="card-text">
                                <small class="text-muted">
                                    ${location}
                                </small>
                            </p>
                            <a href="/businesses/${id}" class="btn btn-dark">View ${title}
                            </a>
                        </div>

                    </div>
                </div>
            </div>
    </div>`;
}