# Importing the Flask Framework

from modules import *
from flask import *
import database
import configparser

page = {}
session = {}

# Initialise the FLASK application
app = Flask(__name__)
app.secret_key = 'SoMeSeCrEtKeYhErE'

# Debug = true if you want debug output on error ; change to false if you dont
app.debug = True

# Read my unikey to show me a personalised app
config = configparser.ConfigParser()
config.read('config.ini')
unikey = config['DATABASE']['user']
portchoice = config['FLASK']['port']


#####################################################
##  INDEX
#####################################################

# What happens when we go to our website
@app.route('/')
def index():
    # If the user is not logged in, then make them go to the login page
    if ('logged_in' not in session or not session['logged_in']):
        return redirect(url_for('login'))
    page['unikey'] = unikey
    page['title'] = 'Welcome'
    return render_template('welcome.html', session=session, page=page)


################################################################################
# Login Page
################################################################################

# This is for the login
# Look at the methods [post, get] that corresponds with form actions etc.
@app.route('/login', methods=['POST', 'GET'])
def login():
    page = {'title': 'Login', 'unikey': unikey}
    # If it's a post method handle it nicely
    if (request.method == 'POST'):
        # Get our login value
        val = database.check_login(request.form['sid'], request.form['password'])

        # If our database connection gave back an error
        if (val == None):
            flash("""Error with the database connection. Please check your terminal
            and make sure you updated your INI files.""")
            return redirect(url_for('login'))

        # If it's null, or nothing came up, flash a message saying error
        # And make them go back to the login screen
        if (val is None or len(val) < 1):
            flash('There was an error logging you in')
            return redirect(url_for('login'))
        # If it was successful, then we can log them in :)
        session['name'] = val[1]
        session['sid'] = request.form['sid']
        session['logged_in'] = True
        return redirect(url_for('index'))
    else:
        # Else, they're just looking at the page :)
        if ('logged_in' in session and session['logged_in'] == True):
            return redirect(url_for('index'))
        return render_template('index.html', page=page)


################################################################################
# Logout Endpoint
################################################################################

@app.route('/logout')
def logout():
    session['logged_in'] = False
    flash('You have been logged out')
    return redirect(url_for('index'))


################################################################################
# Transcript Page
################################################################################

@app.route('/transcript')
def transcript(sid):
    # Go into the database file and get the list_units() function
    transcripts = database.get_transcript(request.form['sid'])
    # What happens if units are null?
    if (transcripts is None):
        # Set it to an empty list and show error message
        transcripts = []
        flash('Error, why there are no transcript of students')
    page['title'] = 'Transcript of students'
    return render_template('transcript.html', page=page, session=session, transcripts=transcripts)


################################################################################
# List Units page
################################################################################

# List the units of study
@app.route('/list-units')
def list_units():
    # Go into the database file and get the list_units() function
    units = database.list_units()

    # What happens if units are null?
    if (units is None):
        # Set it to an empty list and show error message
        units = []
        flash('Error,there are no units of study')
    page['title'] = 'Units of Study'
    return render_template('units.html', page=page, session=session, units=units)


################################################################################
# List Academic Staff
################################################################################

@app.route('/list-academic-staff')
def list_academic_staff():
    academic_staff = database.get_academic_staff()

    if (academic_staff is None):
        academic_staff = []
        flash('Error, there are no academic staff')

    page['title'] = 'Academic staffs'
    return render_template('academicStaff.html', page=page, session=session, academic_staff=academic_staff)


@app.route('/get_staff_in_department')
def get_staff_in_department():
    numberAcademicStaff = database.get_number_of_staff_in_department()

    if (numberAcademicStaff is None):
        numberAcademicStaff = []
        flash('Error, there are no academic staff')

    page['title'] = 'Number of academic staffs'
    return render_template('numberAcademicStaff.html', page=page, session=session,
                           numberAcademicStaff=numberAcademicStaff)


@app.route('/search_staff', methods=["GET", "POST"])
def search_academic_staff():
    page = {'title': 'Search for a staff in particular department'}
    # If it's a post method handle it nicely
    results = []
    if request.method == "POST":
        results = database.search_staff_particular_department(request.form.get("name"), request.form.get("deptid"))

        if (len(results) == 0):
            flash('Error, no staff found')
            return redirect(url_for('search_academic_staff'))
    return render_template('searchResult.html', page=page, session=session, results=results)


@app.route('/add_academic_staff', methods=["GET", "POST"])
def add_academic_staff():
    page = {'title': 'Add a new academic staff'}
    # If it's a post method handle it nicely
    results = []
    if request.method == "POST":
        id = request.form['id']
        if (len(database.searchAcademicStaff(id)) == 1):
            flash('Error, staff id exists')
            return redirect(url_for('add_academic_staff'))

        name = request.form['name']
        deptid = request.form['deptid']
        password = request.form['password']
        address = request.form['address']
        salary = request.form['salary']
        results = database.addAcademicStaff(id, name, deptid, password, address, int(salary))

    return render_template('AddStaff.html', page=page, session=session, results=results)


@app.route('/del_academic_staff', methods=["GET", "POST"])
def del_academic_staff():
    page = {'title': 'Delete a academic staff'}
    # If it's a post method handle it nicely
    results = []
    if request.method == "POST":
        id = request.form['id']
        if (len(database.searchAcademicStaff(id)) != 1):
            flash('Error, staff not exists')
            return redirect(url_for('del_academic_staff'))
        results = database.delAcademicStaff(id)
        flash('Successfully deleted')
    return render_template('DelStaff.html', page=page, session=session, results=results)