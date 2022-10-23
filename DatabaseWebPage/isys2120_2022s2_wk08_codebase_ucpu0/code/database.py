#!/usr/bin/env python3

from modules import pg8000
import configparser


################################################################################
# Connect to the database
#   - This function reads the config file and tries to connect
#   - This is the main "connection" function used to set up our connection
################################################################################

def database_connect():
    # Read the config file
    config = configparser.ConfigParser()
    config.read('config.ini')

    # Create a connection to the database
    connection = None
    try:
        '''
        This is doing a couple of things in the back
        what it is doing is:

        connect(database='y12i2120_yyan6017',
            host='soit-db-pro-2.ucc.usyd.edu.au,
            password='password_from_config',
            user='y19i2120_yyan6017')
        '''
        connection = pg8000.connect(database=config['DATABASE']['database'],
                           user=config['DATABASE']['user'],
                           password=config['DATABASE']['password'],
                           host=config['DATABASE']['host'])
    except pg8000.OperationalError as e:
        print("""Error, you haven't updated your config.ini or you have a bad
        connection, please try again. (Update your files first, then check
        internet connection)
        """)
        print(e)
    except pg8000.ProgrammingError as e:
        print("""Error, config file incorrect: check your password and username""")
        print(e)
    except Exception as e:
        print(e)

    # Return the connection to use
    return connection


################################################################################
# Login Function
#   - This function performs a "SELECT" from the database to check for the
#       student with the same unikey and password as given.
#   - Note: This is only an exercise, there's much better ways to do this
################################################################################


def check_login(sid, pwd):
    # Ask for the database connection, and get the cursor set up
    conn = database_connect()
    if (conn is None):
        return None
    cur = conn.cursor()
    try:
        # Try executing the SQL and get from the database
        sql = """SELECT *
                 FROM unidb.student
                 WHERE studid='%s' AND password='%s'"""%(sid,pwd)
        cur.execute(sql)
        r = cur.fetchone()  # Fetch the first row
        cur.close()  # Close the cursor
        conn.close()  # Close the connection to the db
        return r
    except:
        # If there were any errors, return a NULL row printing an error to the debug
        print("Error Invalid Login")
    cur.close()  # Close the cursor
    conn.close()  # Close the connection to the db
    return None


################################################################################
# List Units
#   - This function performs a "SELECT" from the database to get the unit
#       of study information.
#   - This is useful for your part when we have to make the page.
################################################################################

def list_units():
    # Get the database connection and set up the cursor
    conn = database_connect()
    if (conn is None):
        return None
    # Sets up the rows as a dictionary
    cur = conn.cursor()
    val = None
    try:
        # Try getting all the information returned from the query
        # NOTE: column ordering is IMPORTANT
        cur.execute("""SELECT uosCode, uosName, credits, year, semester
                       FROM UniDB.UoSOffering JOIN UniDB.UnitOfStudy USING (uosCode)
                       ORDER BY uosCode, year, semester""")
        val = cur.fetchall()
    except:
        # If there were any errors, we print something nice and return a NULL value
        print("Error fetching from database")

    cur.close()  # Close the cursor
    conn.close()  # Close the connection to the db
    return val


################################################################################
# Get transcript function
#   - Your turn now!
#   - What do you have to do?
#       1. Connect to the database and set up the cursor.
#       2. You're given an SID - get the transcript for the SID.
#       3. Close the cursor and the connection.
#       4. Return the information we need.
################################################################################

def get_transcript(sid):
    # TODO
    # Get the students transcript from the database
    # You're given an SID as a variable 'sid'
    # Return the results of your query :)
    # Get the database connection and set up the cursor
    conn = database_connect()
    if (conn is None):
        return None
    # Sets up the rows as a dictionary
    cur = conn.cursor()
    val = None
    try:
        # Try getting all the information returned from the query
        # NOTE: column ordering is IMPORTANT
        cur.execute("""SELECT *
                       FROM UniDB.Transcript
                       WHERE Transcript.studId = %s""" % (sid))
        val = cur.fetchall()
    except:
        # If there were any errors, we print something nice and return a NULL value
        print("Error fetching from database")

    cur.close()  # Close the cursor
    conn.close()  # Close the connection to the db
    return val


def get_academic_staff():
    # Get the database connection and set up the cursor
    conn = database_connect()
    if (conn is None):
        return None
    # Sets up the rows as a dictionary
    cur = conn.cursor()
    val = None
    try:
        # Try getting all the information returned from the query
        # NOTE: column ordering is IMPORTANT
        cur.execute("""SELECT id,name,deptid,address
                       FROM unidb.AcademicStaff 
                        """)
        val = cur.fetchall()
    except:
        # If there were any errors, we print something nice and return a NULL value
        print("Error fetching from database")

    cur.close()  # Close the cursor
    conn.close()  # Close the connection to the db
    return val


def search_staff_particular_department(staff, deptid):
    # Get the database connection and set up the cursor
    conn = database_connect()
    if (conn is None):
        return None
    # Sets up the rows as a dictionary
    cur = conn.cursor()
    val = None
    try:
        # Try getting all the information returned from the query
        # NOTE: column ordering is IMPORTANT
        cur.execute("""SELECT id,name,deptid,address
                   FROM unidb.AcademicStaff 
                   WHERE unidb.AcademicStaff.name = '%s'
                   AND unidb.AcademicStaff.deptid = '%s'
                            """ % (staff, deptid))
        val = cur.fetchall()

    except:
        # If there were any errors, we print something nice and return a NULL value
        print("Error fetching from database")

    cur.close()  # Close the cursor
    conn.close()  # Close the connection to the db
    return val


def get_number_of_staff_in_department():
    # Get the database connection and set up the cursor
    conn = database_connect()
    if (conn is None):
        return None
    # Sets up the rows as a dictionary
    cur = conn.cursor()
    val = None
    try:
        # Try getting all the information returned from the query
        # NOTE: column ordering is IMPORTANT
        cur.execute("""SELECT COUNT(deptid),deptid
                        FROM UniDB.AcademicStaff 
                        GROUP BY deptid
                                """)
        val = cur.fetchall()
    except:
        # If there were any errors, we print something nice and return a NULL value
        print("Error fetching from database")

    cur.close()  # Close the cursor
    conn.close()  # Close the connection to the db
    return val


def searchAcademicStaff(id):
    conn = database_connect()
    if (conn is None):
        return None
    cur = conn.cursor()
    val = None
    try:
        cur.execute(""" SELECT id
                        FROM UniDB.AcademicStaff
                        WHERE AcademicStaff.id ='%s'""" % (id))

        val = cur.fetchall()
        print(val, type(val), len(val))
        conn.commit()
    except:
        print("Error fetching from database serach id")

    cur.close()
    conn.close()
    return val


def addAcademicStaff(id, name, deptid, password, address, salary):
    conn = database_connect()
    if (conn is None):
        return None
    cur = conn.cursor()
    val = None

    try:

        cur.execute(""" INSERT INTO UniDB.AcademicStaff
                        VALUES('%s','%s','%s','%s','%s',%s)""" % (id, name, deptid, password, address, salary))

        cur.execute(""" SELECT id,name,deptid,address
                        FROM UniDB.AcademicStaff""")

        val = cur.fetchall()
        conn.commit()
    except:
        print("Error fetching from database add staff")

    cur.close()
    conn.close()
    return val


def delAcademicStaff(id):
    conn = database_connect()
    if (conn is None):
        return None
    cur = conn.cursor()
    val = None
    try:
        cur.execute(""" DELETE FROM UniDB.AcademicStaff
                        WHERE AcademicStaff.id = '%s'""" % id)
        cur.execute(""" SELECT id,name,deptid,address
                        FROM UniDB.AcademicStaff""")
        val = cur.fetchall()
        conn.commit()
    except:
        print("Error fetching from database del staff")

    cur.close()
    conn.close()
    return val


#####################################################
#  Python code if you run it on it's own as 2tier
#####################################################


if (__name__ == '__main__'):
    print("{}\n{}\n{}".format("=" * 50, "Welcome to the 2-Tier Python Database", "=" * 50))

    check_login('307088592', 'Green')
